package ufsm.csi.cpo.modules.credentials;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ufsm.csi.cpo.data.CpoData;
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
    private final CpoData cpoData;
    private String tokenA;

    public CredentialsService(CredentialsTokenService credentialsTokenService) {
        this.credentialsTokenService = credentialsTokenService;
        cpoData = CpoData.getInstance();
    }

    public Credentials getCredentials(String token) throws PlatformNotRegistered {
        if(this.cpoData.getPlatforms().containsKey(token)) {
            return this.cpoData.getPlatforms().get(token).getCredentialsUsed();
        }
        throw new PlatformNotRegistered();
    }

    public void unregisterPlatform(Credentials credentials, String token) throws PlatformNotRegistered {
        var platforms = this.cpoData.getPlatforms();
        if(this.cpoData.getPlatforms().containsKey(token)) {
            platforms.remove(token);
            this.cpoData.getValidCredentialsTokens().remove(this.credentialsTokenService.decodeToken(credentials.getToken()));
        } else throw new PlatformNotRegistered();
    }

    public Optional<Endpoint> getCredentialsEndpoint(Credentials credentials, String token,InterfaceRole otherPlatformRole) throws PlatformAlreadyRegistered, NoMutualVersion, JsonProcessingException {
        if(!this.cpoData.getPlatforms().containsKey(token)) {
            PlatformInfo platformInfo = PlatformInfo.builder()
                    .token(this.credentialsTokenService.decodeToken(credentials.getToken()))
                    .build();
            this.cpoData.getPlatforms().put(token, platformInfo);
            retrieveClientInfo(credentials, token);
            final PlatformInfo updatedPI = this.cpoData.getPlatforms().get(token);
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
        Optional<Endpoint> credentialsEndpointOpt = getCredentialsEndpoint(credentials, token, InterfaceRole.RECEIVER);
        Credentials finalCredentials = null;
        if(credentialsEndpointOpt.isPresent()) {
            var platform = this.cpoData.getPlatforms().get(token);
            Endpoint credentialsEndpoint = credentialsEndpointOpt.get();
            String tokenB = credentialsTokenService.generateToken();
            this.credentialsTokenService.validateToken(tokenB);
            this.cpoData.getPlatforms().remove(token);
            this.cpoData.getPlatforms().put(tokenB, platform);
            finalCredentials = senderLogic(credentialsEndpoint, tokenB, token);
        }
        System.out.println(this.cpoData.getPlatforms().get(token));
        this.cpoData.getPlatforms().get(token).setCredentialsUsed(finalCredentials);
        return finalCredentials;
    }

    public Credentials registerAsReceiver(Credentials credentials, String token) throws PlatformAlreadyRegistered, MalformedURLException, NoMutualVersion, JsonProcessingException {
        Optional<Endpoint> credentialsEndpoint = getCredentialsEndpoint(credentials, token, InterfaceRole.SENDER);
        String tokenC = "";
        var platform = this.cpoData.getPlatforms().get(token);
        if(credentialsEndpoint.isPresent()) {
           tokenC = credentialsTokenService.generateToken();
           this.credentialsTokenService.validateToken(tokenC);
           this.cpoData.getPlatforms().remove(token);
           this.cpoData.getPlatforms().put(tokenC, platform);
           this.credentialsTokenService.invalidateToken(this.tokenA);
        }
        System.out.println(this.cpoData.getPlatforms().get(tokenC));
        var credentialsRole = CredentialsRole.builder()
                .role(Role.CPO)
                .partyId(new CiString("PSI"))
                .countryCode(new CiString("BR"))
                .build();
        var cpoCredentials = Credentials.builder()
                .url(new URL(this.cpoData.getServerUrl() + "/ocpi/cpo/versions"))
                .token(this.credentialsTokenService.encodeToken(tokenC))
                .roles(Arrays.asList(credentialsRole))
                .build();
        platform.setCredentialsUsed(cpoCredentials);
        return cpoCredentials;
    }

    @SneakyThrows
    public Credentials senderLogic(Endpoint endpoint, String tokenB, String token) {
        CredentialsRole credentialsRole = CredentialsRole.builder()
                .role(Role.CPO)
                .partyId(new CiString("PSI"))
                .countryCode(new CiString("BR"))
                .build();
        Credentials emspCredentials = Credentials.builder()
                .url(new URL(this.cpoData.getServerUrl() + "/ocpi/cpo/versions"))
                .token(this.credentialsTokenService.encodeToken(tokenB))
                .roles(Arrays.asList(credentialsRole))
                .build();
        PlatformInfo platformInfo1 = this.cpoData.getPlatforms().get(token);
        ObjectMapper objectMapper = new ObjectMapper();
        Credentials cpoCredentials = objectMapper.readValue(httpRequest(endpoint.getUrl(), "POST", platformInfo1.getToken(), emspCredentials), Credentials.class);
        String tokenC = cpoCredentials.getToken();
        platformInfo1.setToken(tokenC);
        return emspCredentials;
    }

    public Optional<VersionNumber> pickLatestMutualVersion(List<Version> lhs, List<Version> rhs) throws NoMutualVersion {
        List<VersionNumber> mutualVersions = new ArrayList<>();
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
        VersionNumber latest = VersionNumber.min();
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
            String response = httpRequest(credentials.getUrl(), "GET", this.credentialsTokenService.decodeToken(credentials.getToken()));
            ObjectMapper objectMapper = new ObjectMapper();
            List<Version> versions = objectMapper.readValue(response, new TypeReference<List<Version>>() {});
            Optional<VersionNumber> latestMutualVersionNumberOpt = pickLatestMutualVersion(this.cpoData.getVersions(), versions);
            if(latestMutualVersionNumberOpt.isPresent()) {
                VersionNumber latestMutualVersionNumber = latestMutualVersionNumberOpt.get();
                Optional<Version> compatibleVersionOpt = versions.stream()
                        .filter(v -> v.getVersion().equals(latestMutualVersionNumber))
                        .findFirst();
                if (compatibleVersionOpt.isPresent()) {
                    Version compatibleVersion = compatibleVersionOpt.get();
                    response = httpRequest(compatibleVersion.getUrl(), "GET", this.credentialsTokenService.decodeToken(credentials.getToken()));
                    List<VersionDetails> versionDetails = objectMapper.readValue(response, new TypeReference<List<VersionDetails>>() {
                    });
                    PlatformInfo platformInfo = this.cpoData.getPlatforms().get(token);
                    platformInfo.setVersions(versionDetails);
                    platformInfo.setCurrentVersion(latestMutualVersionNumber);
                    this.cpoData.getPlatforms().put(token, platformInfo);
                }
            }
    }

    @SneakyThrows
    public String httpRequest(URL url, String method, String token) {
        System.out.println("Sending " + method + " request to: " + url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Authorization", "Token " + credentialsTokenService.encodeToken(token));
        int responseCode = connection.getResponseCode();
        StringBuilder sb = new StringBuilder();
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
        }
        return String.valueOf(sb);
    }

    @SneakyThrows
    public String httpRequest(URL url, String method, String token, Credentials credentials) {
        System.out.println("Sending " + method + " request to: " + url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Authorization", "Token " + credentialsTokenService.encodeToken(token));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        ObjectMapper om = new ObjectMapper();
        String credentialsJson = om.writeValueAsString(credentials);
        try (OutputStream os = connection.getOutputStream();
             OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            osw.write(credentialsJson);
            osw.flush();
        } catch(Error e) {
            e.printStackTrace();
        }
        connection.connect();

        int responseCode = connection.getResponseCode();
        StringBuilder sb = new StringBuilder();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
        }
        return String.valueOf(sb);
    }
}
