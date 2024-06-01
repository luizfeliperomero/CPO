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
import ufsm.csi.cpo.modules.types.Role;
import ufsm.csi.cpo.modules.versions.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
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

    public void unregisterPlatform(Credentials credentials, String token) throws PlatformNotRegistered {
        var platforms = this.platformData.getPlatforms();
        if(this.platformData.getPlatforms().containsKey(token)) {
            platforms.remove(token);
            this.platformData.getValidCredentialsTokens().remove(this.credentialsTokenService.decodeToken(credentials.getToken()));
        } else throw new PlatformNotRegistered();
    }

    public Optional<Endpoint> getCredentialsEndpoint(Credentials credentials, String token,InterfaceRole otherPlatformRole) throws PlatformAlreadyRegistered, NoMutualVersion, JsonProcessingException {
        if(!this.platformData.getPlatforms().containsKey(token)) {
            var platformInfo = PlatformInfo.builder()
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

    public Credentials registerAsSender(Credentials credentials, String token) throws PlatformAlreadyRegistered, NoMutualVersion, JsonProcessingException {
        var credentialsEndpointOpt = getCredentialsEndpoint(credentials, token, InterfaceRole.RECEIVER);
        Credentials finalCredentials = null;
        if(credentialsEndpointOpt.isPresent()) {
            var platform = this.platformData.getPlatforms().get(token);
            Endpoint credentialsEndpoint = credentialsEndpointOpt.get();
            String tokenB = credentialsTokenService.generateToken();
            this.credentialsTokenService.validateToken(tokenB);
            this.platformData.getPlatforms().remove(token);
            this.platformData.getPlatforms().put(tokenB, platform);
            finalCredentials = senderLogic(credentialsEndpoint, tokenB, token);
        }
        System.out.println(this.platformData.getPlatforms().get(token));
        this.platformData.getPlatforms().get(token).setCredentialsUsed(finalCredentials);
        return finalCredentials;
    }

    public Credentials registerAsReceiver(Credentials credentials, String token) throws PlatformAlreadyRegistered, MalformedURLException, NoMutualVersion, JsonProcessingException {
        var credentialsEndpointOpt = getCredentialsEndpoint(credentials, token, InterfaceRole.SENDER);
        var tokenC = "";
        var platform = this.platformData.getPlatforms().get(token);
        if(credentialsEndpointOpt.isPresent()) {
           tokenC = credentialsTokenService.generateToken();
           this.credentialsTokenService.validateToken(tokenC);
           this.platformData.getPlatforms().remove(token);
           this.platformData.getPlatforms().put(tokenC, platform);
           this.credentialsTokenService.invalidateToken(this.tokenA);
        }
        System.out.println(this.platformData.getPlatforms().get(tokenC));
        var credentialsRole = CredentialsRole.builder()
                .role(Role.CPO)
                .partyId(new CiString("PSI"))
                .countryCode(new CiString("BR"))
                .build();
        var platformCredentials = Credentials.builder()
                .url(new URL(this.platformData.getServerUrl() + "/ocpi/cpo/versions"))
                .token(this.credentialsTokenService.encodeToken(tokenC))
                .roles(Arrays.asList(credentialsRole))
                .build();
        platform.setCredentialsUsed(platformCredentials);
        return platformCredentials;
    }

    @SneakyThrows
    public Credentials senderLogic(Endpoint endpoint, String tokenB, String token) {
        var credentialsRole = CredentialsRole.builder()
                .role(Role.CPO)
                .partyId(new CiString("PSI"))
                .countryCode(new CiString("BR"))
                .build();
        var credentials = Credentials.builder()
                .url(new URL(this.platformData.getServerUrl() + "/ocpi/cpo/versions"))
                .token(this.credentialsTokenService.encodeToken(tokenB))
                .roles(Arrays.asList(credentialsRole))
                .build();
        var platformInfo = this.platformData.getPlatforms().get(token);
        var objectMapper = new ObjectMapper();
        var otherPlatformCredentials = objectMapper.readValue(httpRequest(endpoint.getUrl(), "POST", platformInfo.getToken(), credentials), Credentials.class);
        var tokenC = otherPlatformCredentials.getToken();
        platformInfo.setToken(tokenC);
        return credentials;
    }

    public Optional<VersionNumber> pickLatestMutualVersion(List<Version> lhs, List<Version> rhs) throws NoMutualVersion {
        var mutualVersions = new ArrayList<VersionNumber>();
        lhs.forEach(vlhs -> {
            rhs.forEach(vrhs -> {
                if (vlhs.getVersion().equals(vrhs.getVersion())) {
                    mutualVersions.add(vlhs.getVersion());
                }
            });
        });
        if (!mutualVersions.isEmpty()) {
            return Optional.of(getLatestVersion(mutualVersions));
        } else throw new NoMutualVersion("No mutual version to establish communication");
    }

    public VersionNumber getLatestVersion(List<VersionNumber> versions) {
        var latest = VersionNumber.min();
        for(var version : versions) {
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


    public void retrieveClientInfo(Credentials credentials, String token) throws NoMutualVersion, JsonProcessingException {
            var response = httpRequest(credentials.getUrl(), "GET", this.credentialsTokenService.decodeToken(credentials.getToken()));
            var objectMapper = new ObjectMapper();
            var versions = objectMapper.readValue(response, new TypeReference<List<Version>>() {});
            var latestMutualVersionNumberOpt = pickLatestMutualVersion(this.platformData.getVersions(), versions);
            if(latestMutualVersionNumberOpt.isPresent()) {
                VersionNumber latestMutualVersionNumber = latestMutualVersionNumberOpt.get();
                var compatibleVersionOpt = versions.stream()
                        .filter(v -> v.getVersion().equals(latestMutualVersionNumber))
                        .findFirst();
                if (compatibleVersionOpt.isPresent()) {
                    var compatibleVersion = compatibleVersionOpt.get();
                    response = httpRequest(compatibleVersion.getUrl(), "GET", this.credentialsTokenService.decodeToken(credentials.getToken()));
                    var versionDetails = objectMapper.readValue(response, new TypeReference<List<VersionDetails>>() {
                    });
                    var platformInfo = this.platformData.getPlatforms().get(token);
                    platformInfo.setVersions(versionDetails);
                    platformInfo.setCurrentVersion(latestMutualVersionNumber);
                    this.platformData.getPlatforms().put(token, platformInfo);
                }
            }
    }

    @SneakyThrows
    public String httpRequest(URL url, String method, String token) {
        System.out.println("Sending " + method + " request to: " + url);
        var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Authorization", "Token " + credentialsTokenService.encodeToken(token));
        var responseCode = connection.getResponseCode();
        var sb = new StringBuilder();
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            var scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
        }
        return String.valueOf(sb);
    }

    @SneakyThrows
    public String httpRequest(URL url, String method, String token, Credentials credentials) {
        System.out.println("Sending " + method + " request to: " + url);
        var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Authorization", "Token " + credentialsTokenService.encodeToken(token));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        var om = new ObjectMapper();
        var credentialsJson = om.writeValueAsString(credentials);
        try (var os = connection.getOutputStream();
             var osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            osw.write(credentialsJson);
            osw.flush();
        } catch(Error e) {
            e.printStackTrace();
        }
        connection.connect();

        var responseCode = connection.getResponseCode();
        var sb = new StringBuilder();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
        }
        return String.valueOf(sb);
    }
}
