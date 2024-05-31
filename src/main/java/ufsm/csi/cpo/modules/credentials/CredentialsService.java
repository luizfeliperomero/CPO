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

    public Optional<Endpoint> getCredentialsEndpoint(Credentials credentials, InterfaceRole otherPlatformRole) throws PlatformAlreadyRegistered, NoMutualVersion, JsonProcessingException {
        CiString partyId = credentials.getRoles().get(0).getPartyId();
        Optional<Endpoint> endpointOpt = Optional.empty();
        if(!this.cpoData.getPlatforms().containsKey(partyId)) {
            PlatformInfo platformInfo = PlatformInfo.builder()
                    .token(this.credentialsTokenService.decodeToken(credentials.getToken()))
                    .build();
            this.cpoData.getPlatforms().put(partyId, platformInfo);
            retrieveClientInfo(credentials, partyId);
            PlatformInfo updatedPI = this.cpoData.getPlatforms().get(partyId);
            Optional<VersionDetails> versionDetailsOpt = updatedPI.getVersions()
                    .stream()
                    .filter(v -> v.getVersion().equals(updatedPI.getCurrentVersion()))
                    .findFirst();
            if(versionDetailsOpt.isPresent()) {
                VersionDetails versionDetails = versionDetailsOpt.get();
                endpointOpt = versionDetails.getEndpoints()
                        .stream()
                        .filter(e -> e.getIdentifier().equals(ModuleID.credentials) && e.getRole().equals(otherPlatformRole))
                        .findFirst();
            }
        } else throw new PlatformAlreadyRegistered("Platform of party id: " +partyId + " has already been registered");
        return endpointOpt;
    }

    public Credentials exchangeCredentialsAsSender(Credentials credentials) throws PlatformAlreadyRegistered, NoMutualVersion, JsonProcessingException {
        CiString partyId = credentials.getRoles().get(0).getPartyId();
        Optional<Endpoint> credentialsEndpointOpt = getCredentialsEndpoint(credentials, InterfaceRole.RECEIVER);
        Credentials finalCredentials = null;
        if(credentialsEndpointOpt.isPresent()) {
            Endpoint credentialsEndpoint = credentialsEndpointOpt.get();
            String tokenB = credentialsTokenService.generateToken();
            this.credentialsTokenService.validateToken(tokenB);
            finalCredentials = senderLogic(credentialsEndpoint, tokenB, partyId);
        }
        System.out.println(this.cpoData.getPlatforms().get(partyId));
        return finalCredentials;
    }

    public Credentials exchangeCredentialsAsReceiver(Credentials credentials) throws PlatformAlreadyRegistered, MalformedURLException, NoMutualVersion, JsonProcessingException {
        CiString partyId = credentials.getRoles().get(0).getPartyId();
        Optional<Endpoint> credentialsEndpoint = getCredentialsEndpoint(credentials, InterfaceRole.SENDER);
        String tokenC = "";
        if(credentialsEndpoint.isPresent()) {
           tokenC = credentialsTokenService.generateToken();
           this.credentialsTokenService.validateToken(tokenC);
           this.credentialsTokenService.invalidateToken(this.tokenA);
        }
        System.out.println(this.cpoData.getPlatforms().get(partyId));
        CredentialsRole credentialsRole = CredentialsRole.builder()
                .role(Role.CPO)
                .partyId(this.cpoData.getPartyId())
                .countryCode(this.cpoData.getCountryCode())
                .build();
        Credentials cpoCredentials = Credentials.builder()
                .url(new URL(this.cpoData.getServerUrl() + "/ocpi/cpo/versions"))
                .token(this.credentialsTokenService.encodeToken(tokenC))
                .roles(Arrays.asList(credentialsRole))
                .build();
        return cpoCredentials;
    }

    @SneakyThrows
    public Credentials senderLogic(Endpoint endpoint, String tokenB, CiString partyId) {
        CredentialsRole credentialsRole = CredentialsRole.builder()
                .role(Role.CPO)
                .partyId(this.cpoData.getPartyId())
                .countryCode(this.cpoData.getCountryCode())
                .build();
        Credentials emspCredentials = Credentials.builder()
                .url(new URL(this.cpoData.getServerUrl() + "/ocpi/cpo/versions"))
                .token(this.credentialsTokenService.encodeToken(tokenB))
                .roles(Arrays.asList(credentialsRole))
                .build();
        PlatformInfo platformInfo1 = this.cpoData.getPlatforms().get(partyId);
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




    public void retrieveClientInfo(Credentials credentials, CiString partyId) throws NoMutualVersion, JsonProcessingException {
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
                    PlatformInfo platformInfo = this.cpoData.getPlatforms().get(partyId);
                    platformInfo.setVersions(versionDetails);
                    platformInfo.setCurrentVersion(latestMutualVersionNumber);
                    this.cpoData.getPlatforms().put(partyId, platformInfo);
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
