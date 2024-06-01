package ufsm.csi.cpo.data;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.Value;
import org.springframework.stereotype.Service;
import ufsm.csi.cpo.modules.credentials.Credentials;
import ufsm.csi.cpo.modules.credentials.PlatformInfo;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.versions.*;

import java.net.URL;
import java.util.*;

@Data
public class CpoData {
    private static CpoData instance;
    private Map<String, PlatformInfo> platforms = new HashMap<>();
    private List<String> validCredentialsTokens = new ArrayList<>();
    private final List<Version> versions;
    private final List<VersionDetails> versionDetails ;
    private final String serverUrl;

    @SneakyThrows
    private CpoData() {
       this.versions = Arrays.asList(new Version(VersionNumber.V2_2_1, new URL("http://localhost:8080/ocpi/cpo/versions/2.2.1/details")));
       this.serverUrl = "http://localhost:8080";
        List<Endpoint> endpoints = Arrays.asList(
                new Endpoint(ModuleID.credentials,
                        InterfaceRole.SENDER,
                        new URL("http://localhost:8080/2.2.1/credentials/sender")),
                new Endpoint(ModuleID.credentials,
                        InterfaceRole.RECEIVER,
                        new URL("http://localhost:8080/2.2.1/credentials/receiver")));
        this.versionDetails = Arrays.asList(new VersionDetails(VersionNumber.V2_2_1, endpoints));
    }

    public static CpoData getInstance() {
        synchronized (CpoData.class) {
            if(instance == null) {
                instance = new CpoData();
            }
        }
        return instance;
    }
}
