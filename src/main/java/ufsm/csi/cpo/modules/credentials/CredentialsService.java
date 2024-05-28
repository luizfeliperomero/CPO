package ufsm.csi.cpo.modules.credentials;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ufsm.csi.cpo.modules.tokens.Token;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.Role;
import ufsm.csi.cpo.modules.versions.*;
import ufsm.csi.cpo.security.JwtService;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CredentialsService {
    private Map<CiString, PlatformInfo> platforms;
    private final JwtService jwtService;

    public CredentialsService(JwtService jwtService) {
        this.jwtService = jwtService;
        platforms = new HashMap<>();
    }

    public Optional<Endpoint> getCredentialsEndpoint(Credentials credentials, InterfaceRole otherPlatformRole) {
        CiString partyId = credentials.getRoles().get(0).getPartyId();
        Optional<Endpoint> endpointOpt = Optional.empty();
        if(!platforms.containsKey(partyId)) {
            PlatformInfo platformInfo = PlatformInfo.builder()
                    .token(credentials.getToken())
                    .build();
            platforms.put(partyId, platformInfo);
            retrieveClientInfo(credentials, partyId);
            Optional<VersionDetails> versionDetailsOpt = platforms.get(partyId).getVersions()
                    .stream()
                    .filter(v -> v.getVersion().toString().equals("2.2.1"))
                    .findFirst();
            if(versionDetailsOpt.isPresent()) {
                VersionDetails versionDetails = versionDetailsOpt.get();
                endpointOpt = versionDetails.getEndpoints()
                        .stream()
                        .filter(e -> e.getIdentifier().equals(ModuleID.credentials) && e.getRole().equals(otherPlatformRole))
                        .findFirst();
            }
        }
        return endpointOpt;
    }

    public void exchangeCredentialsAsSender(Credentials credentials) {
        CiString partyId = credentials.getRoles().get(0).getPartyId();
        Optional<Endpoint> credentialsEndpointOpt = getCredentialsEndpoint(credentials, InterfaceRole.RECEIVER);
        if(credentialsEndpointOpt.isPresent()) {
            Endpoint credentialsEndpoint = credentialsEndpointOpt.get();
            String tokenB = jwtService.generateToken();
            senderLogic(credentialsEndpoint, tokenB, partyId);
        }
        System.out.println(platforms.get(partyId));
    }

    public String exchangeCredentialsAsReceiver(Credentials credentials) {
        CiString partyId = credentials.getRoles().get(0).getPartyId();
        Optional<Endpoint> credentialsEndpoint = getCredentialsEndpoint(credentials, InterfaceRole.SENDER);
        String tokenC = "";
        if(credentialsEndpoint.isPresent()) {
           tokenC = jwtService.generateToken();
        }
        System.out.println(platforms.get(partyId));
        return tokenC;
    }

    @SneakyThrows
    public void senderLogic(Endpoint endpoint, String tokenB, CiString partyId) {
        CredentialsRole credentialsRole = CredentialsRole.builder()
                .role(Role.CPO)
                .partyId(new CiString("psi"))
                .countryCode(new CiString("br"))
                .build();
        Credentials emspCredentials = Credentials.builder()
                .url(new URL("http://localhost:8080/ocpi/cpo/versions"))
                .token(tokenB)
                .roles(Arrays.asList(credentialsRole))
                .build();
        String tokenC = httpRequest(endpoint.getUrl(), "POST", tokenB, emspCredentials);
        PlatformInfo platformInfo1 = platforms.get(partyId);
        platformInfo1.setToken(tokenC);
    }

    @SneakyThrows
    public void retrieveClientInfo(Credentials credentials, CiString partyId) {
            String response = httpRequest(credentials.getUrl(), "GET", credentials.getToken());
            ObjectMapper objectMapper = new ObjectMapper();
            List<Version> versions = objectMapper.readValue(response, new TypeReference<List<Version>>() {});
            Optional<Version> compatibleVersionOpt = versions.stream()
                    .filter(v -> v.getVersion().toString().equals("2.2.1"))
                    .findFirst();
            if(compatibleVersionOpt.isPresent()) {
                Version compatibleVersion = compatibleVersionOpt.get();
                response = httpRequest(compatibleVersion.getUrl(), "GET", credentials.getToken());
                List<VersionDetails> versionDetails = objectMapper.readValue(response, new TypeReference<List<VersionDetails>>() {});
                PlatformInfo platformInfo = platforms.get(partyId);
                platformInfo.setVersions(versionDetails);
                platforms.put(partyId, platformInfo);
            }
    }

    @SneakyThrows
    public String httpRequest(URL url, String method, String token) {
        System.out.println("Sending " + method + " request to: " + url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Authorization", "Bearer " + token);
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
        connection.setRequestProperty("Authorization", "Bearer " + token);
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
