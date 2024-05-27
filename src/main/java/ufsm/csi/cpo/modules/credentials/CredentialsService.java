package ufsm.csi.cpo.modules.credentials;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.versions.Version;
import ufsm.csi.cpo.modules.versions.VersionDetails;
import ufsm.csi.cpo.modules.versions.VersionNumber;
import ufsm.csi.cpo.security.JwtService;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class CredentialsService {
    private Map<CiString, PlatformInfo> platforms;
    private final JwtService jwtService;

    public CredentialsService(JwtService jwtService) {
        platforms = new HashMap<>();
        this.jwtService = jwtService;
    }

    public String exchangeCredentials(Credentials credentials) {
        CiString partyId = credentials.getRoles().get(0).getPartyId();
        String tokenC = "";
        if(!platforms.containsKey(partyId)) {
            PlatformInfo platformInfo = PlatformInfo.builder()
                    .token(credentials.getToken())
                    .versions(Arrays.asList(new VersionDetails()))
                    .build();
            platforms.put(partyId, platformInfo);
            retrieveClientInfo(credentials, partyId);
            tokenC = jwtService.generateToken();
        }
        return tokenC;
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
}
