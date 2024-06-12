package ufsm.csi.cpo.modules.credentials;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ufsm.csi.cpo.data.PlatformData;
import ufsm.csi.cpo.exceptions.NoMutualVersion;
import ufsm.csi.cpo.exceptions.PlatformAlreadyRegistered;
import ufsm.csi.cpo.exceptions.PlatformNotRegistered;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.Response;
import ufsm.csi.cpo.modules.types.Role;
import ufsm.csi.cpo.modules.versions.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Data
public class CredentialsService {
    private final CredentialsTokenService credentialsTokenService;
    private final PlatformData platformData;
    private final VersionNumber versionNumber = VersionNumber.V2_1_1;
    private String tokenA;

    public CredentialsService(CredentialsTokenService credentialsTokenService) {
        this.credentialsTokenService = credentialsTokenService;
        platformData = PlatformData.getInstance();
    }

    public Credentials getCredentials(String token) throws PlatformNotRegistered {
        if(this.platformData.getPlatforms().containsKey(token)) {
            return this.platformData.getPlatforms().get(token).getCredentialsUsed();
        }
        throw new PlatformNotRegistered();
    }

    public void unregisterPlatform(final String token) throws PlatformNotRegistered {
        var platforms = this.platformData.getPlatforms();
        if(this.platformData.getPlatforms().containsKey(token)) {
            platforms.remove(token);
        } else throw new PlatformNotRegistered();
    }

    public Optional<Endpoint> getCredentialsEndpoint(final Credentials credentials, final String token, final InterfaceRole otherPlatformRole) throws PlatformAlreadyRegistered, NoMutualVersion, JsonProcessingException {
        if(!this.platformData.getPlatforms().containsKey(token)) {
            final var platformInfo = PlatformInfo.builder()
                    .token(this.credentialsTokenService.decodeToken(credentials.getToken()))
                    .build();
            this.platformData.getPlatforms().put(token, platformInfo);
            retrieveClientInfo(credentials, token);
            final PlatformInfo updatedPI = this.platformData.getPlatforms().get(token);
            return updatedPI.getVersions()
                    .stream()
                    .filter(v -> v.getVersion().equals(updatedPI.getCurrentVersion()))
                    .findFirst()
                    .flatMap(vd -> vd.getEndpoints().stream()
                            .filter(e -> e.getIdentifier().equals(ModuleID.credentials) && e.getRole().equals(otherPlatformRole))
                            .findFirst()
                    );

        } else throw new PlatformAlreadyRegistered();
    }

    public Map.Entry<String, PlatformInfo> getPlatformByCredentials(final Credentials credentials) throws PlatformNotRegistered {
        final var platformInfoOpt = this.platformData.getPlatforms().entrySet().stream()
                .filter(e -> e.getValue().getToken().equals(credentials.getToken()))
                .findFirst();
        if(platformInfoOpt.isEmpty()) throw new PlatformNotRegistered();
        return platformInfoOpt.get();
    }

    public Credentials registerAsSender(final Credentials credentials, final String tokenB) throws PlatformAlreadyRegistered, NoMutualVersion, JsonProcessingException {
        final var credentialsEndpointOpt = getCredentialsEndpoint(credentials, tokenB, InterfaceRole.RECEIVER);
        Credentials finalCredentials = null;
        final var platform = this.platformData.getPlatforms().get(tokenB);
        final var platforms = this.platformData.getPlatforms();
        if(credentialsEndpointOpt.isPresent()) {
            final var credentialsEndpoint = credentialsEndpointOpt.get();
            this.credentialsTokenService.validateToken(tokenB);
            platforms.remove(tokenB);
            platforms.put(tokenB, platform);
            finalCredentials = senderLogic(credentialsEndpoint, tokenB);
        }
        System.out.println(platform);
        platform.setCredentialsUsed(finalCredentials);
        platform.setCredentials(finalCredentials);
        return finalCredentials;
    }

    public Credentials registerAsReceiver(final Credentials credentials, final String token) throws PlatformAlreadyRegistered, MalformedURLException, NoMutualVersion, JsonProcessingException {
        final var credentialsEndpointOpt = getCredentialsEndpoint(credentials, token, InterfaceRole.SENDER);
        var tokenC = "";
        final var platform = this.platformData.getPlatforms().get(token);
        if(credentialsEndpointOpt.isPresent()) {
           tokenC = credentialsTokenService.generateToken();
           this.credentialsTokenService.validateToken(tokenC);
           this.platformData.getPlatforms().remove(token);
           this.platformData.getPlatforms().put(tokenC, platform);
           this.credentialsTokenService.invalidateToken(this.tokenA);
        }
        System.out.println(this.platformData.getPlatforms().get(tokenC));
        final var credentialsRole = CredentialsRole.builder()
                .role(Role.CPO)
                .partyId(new CiString("PSI"))
                .countryCode(new CiString("BR"))
                .build();
        final var platformCredentials = Credentials.builder()
                .url(new URL(this.platformData.getServerUrl() + "/ocpi/cpo/versions"))
                .token(this.credentialsTokenService.encodeToken(tokenC))
                .roles(Arrays.asList(credentialsRole))
                .build();
        platform.setCredentialsUsed(platformCredentials);
        platform.setCredentials(platformCredentials);
        return platformCredentials;
    }

    public void updateVersionAsSender(final String token) throws PlatformNotRegistered, JsonProcessingException, NoMutualVersion {
        if(this.platformData.getPlatforms().containsKey(token)) {
            final var platformInfo = this.platformData.getPlatforms().get(token);
            final var otherPlatformCredentials = platformInfo.getCredentials();
            final var objectMapper = new ObjectMapper();
            final var versions = objectMapper.readValue(httpRequest(otherPlatformCredentials.getUrl(), "GET", otherPlatformCredentials.getToken()), new TypeReference<List<Version>>() {});
            final var latestMutualVersion = getLatestMutualVersion(versions);
            final var versionDetails = objectMapper.readValue(httpRequest(latestMutualVersion.getUrl(), "GET", otherPlatformCredentials.getToken()), new TypeReference<List<VersionDetails>>() {});
            platformInfo.setVersions(versionDetails);
            platformInfo.setCurrentVersion(latestMutualVersion.getVersion());
            final var lastMutualVersionDetailOpt = versionDetails.stream()
                    .filter(vd -> vd.getVersion().equals(latestMutualVersion.getVersion()))
                    .findFirst();
            if (lastMutualVersionDetailOpt.isPresent()) {
                final var lastMutualVersionDetail = lastMutualVersionDetailOpt.get();
                final var credentialsEndpointOpt = lastMutualVersionDetail.getEndpoints().stream()
                        .filter(e -> e.getIdentifier().equals(ModuleID.credentials) && e.getRole().equals(InterfaceRole.RECEIVER))
                        .findFirst();
                if (credentialsEndpointOpt.isPresent()) {
                    final var credentialsEndpoint = credentialsEndpointOpt.get();
                    final var receiverUpdatedCredentials = objectMapper.readValue(httpRequest(credentialsEndpoint.getUrl(), "PUT", otherPlatformCredentials.getToken()), Credentials.class);
                    platformInfo.setCredentials(receiverUpdatedCredentials);
                }
            }
        }
    }

    public Credentials updateVersionAsReceiver(final String token) throws JsonProcessingException, NoMutualVersion {
        final var platformInfo = this.platformData.getPlatforms().get(token);
        final var credentials = platformInfo.getCredentials();
        final var objectMapper = new ObjectMapper();
        final var versions = objectMapper.readValue(httpRequest(credentials.getUrl(), "GET", credentials.getToken()), new TypeReference<List<Version>>() {});
        final var versionOpt = versions.stream()
                .filter(v -> v.getVersion().equals(versionNumber))
                .findFirst();
        if(versionOpt.isPresent()) {
            final var version = versionOpt.get();
            final var versionDetails = objectMapper.readValue(httpRequest(version.getUrl(), "GET", credentials.getToken()), new TypeReference<List<VersionDetails>>() {});
            platformInfo.setVersions(versionDetails);
            platformInfo.setCurrentVersion(versionNumber);
            return platformInfo.getCredentials();
        }
        throw new NoMutualVersion("Couldn't find a compatible version to complete update");
    }

    public Version getLatestMutualVersion(final List<Version> versions) throws NoMutualVersion {
        final var latestVersion = pickLatestMutualVersion(versions, this.platformData.getVersions());
        final var versionOpt = versions.stream()
                .filter(v -> v.getVersion().equals(latestVersion))
                .findFirst();
        if(versionOpt.isPresent()) return versionOpt.get();
        throw new NoMutualVersion("Couldn't find a version match");
}

    @SneakyThrows
    public Credentials senderLogic(final Endpoint endpoint, final String tokenB) {
        final var credentialsRole = CredentialsRole.builder()
                .role(Role.CPO)
                .partyId(new CiString("PSI"))
                .countryCode(new CiString("BR"))
                .build();
        final var credentials = Credentials.builder()
                .url(new URL(this.platformData.getServerUrl() + "/ocpi/cpo/versions"))
                .token(this.credentialsTokenService.encodeToken(tokenB))
                .roles(Arrays.asList(credentialsRole))
                .build();
        final var platformInfo = this.platformData.getPlatforms().get(tokenB);
        final var objectMapper = new ObjectMapper();
        final var typeReference = new TypeReference<Response<Credentials>>(){};
        final var otherPlatformCredentialsResponse = objectMapper.readValue(httpRequest(endpoint.getUrl(), "POST", platformInfo.getToken(), credentials), typeReference);
        final var otherPlatformCredentials = otherPlatformCredentialsResponse.getData();
        final var tokenC = otherPlatformCredentials.getToken();
        platformInfo.setToken(tokenC);
        return credentials;
    }

    public VersionNumber pickLatestMutualVersion(final List<Version> lhs, final List<Version> rhs) throws NoMutualVersion {
        final var mutualVersions = new ArrayList<VersionNumber>();
        lhs.forEach(vlhs -> {
            rhs.forEach(vrhs -> {
                if (vlhs.getVersion().equals(vrhs.getVersion())) {
                    mutualVersions.add(vlhs.getVersion());
                }
            });
        });
        if (!mutualVersions.isEmpty()) {
            return getLatestVersion(mutualVersions);
        } else throw new NoMutualVersion("No mutual version to establish communication");
    }

    public VersionNumber getLatestVersion(final List<VersionNumber> versions) {
        var latest = VersionNumber.min();
        for(final var version : versions) {
            if(version.equals(VersionNumber.max())) {
                latest = version;
            } else {
                if(VersionNumber.isGreater(version, latest)) {
                    latest = version;
                }
            }
        }
        return latest;
    }


    public void retrieveClientInfo(final Credentials credentials, final String token) throws NoMutualVersion, JsonProcessingException {
            var response = httpRequest(credentials.getUrl(), "GET", this.credentialsTokenService.decodeToken(credentials.getToken()));
            final var objectMapper = new ObjectMapper();
            final var versions = objectMapper.readValue(response, new TypeReference<List<Version>>() {});
            final var latestMutualVersionNumber = pickLatestMutualVersion(this.platformData.getVersions(), versions);
            final var compatibleVersionOpt = versions.stream()
                    .filter(v -> v.getVersion().equals(latestMutualVersionNumber))
                    .findFirst();
            if (compatibleVersionOpt.isPresent()) {
                final var compatibleVersion = compatibleVersionOpt.get();
                response = httpRequest(compatibleVersion.getUrl(), "GET", this.credentialsTokenService.decodeToken(credentials.getToken()));
                final var versionDetails = objectMapper.readValue(response, new TypeReference<List<VersionDetails>>() {
                });
                final var platformInfo = this.platformData.getPlatforms().get(token);
                platformInfo.setVersions(versionDetails);
                platformInfo.setCurrentVersion(latestMutualVersionNumber);
                this.platformData.getPlatforms().put(token, platformInfo);
            }
    }

    @SneakyThrows
    public String httpRequest(final URL url, final String method, final String token) {
        System.out.println("Sending " + method + " request to: " + url);
        final var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Authorization", "Token " + credentialsTokenService.encodeToken(token));
        final var responseCode = connection.getResponseCode();
        final var sb = new StringBuilder();
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            final var scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
        }
        return String.valueOf(sb);
    }

    @SneakyThrows
    public String httpRequest(final URL url, final String method, final String token, final Credentials credentials) {
        System.out.println("Sending " + method + " request to: " + url);
        final var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Authorization", "Token " + credentialsTokenService.encodeToken(token));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        final var om = new ObjectMapper();
        final var credentialsJson = om.writeValueAsString(credentials);
        try (final var os = connection.getOutputStream();
             final var osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            osw.write(credentialsJson);
            osw.flush();
        } catch(Error e) {
            e.printStackTrace();
        }
        connection.connect();

        final var responseCode = connection.getResponseCode();
        final var sb = new StringBuilder();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            final Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
        }
        return String.valueOf(sb);
    }
}
