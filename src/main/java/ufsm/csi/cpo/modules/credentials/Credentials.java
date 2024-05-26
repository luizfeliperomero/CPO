package ufsm.csi.cpo.modules.credentials;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.versions.URL;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Credentials {
    private String token;
    private URL url;
    private List<CredentialsRole> roles;
}