package ufsm.csi.cpo.modules.credentials;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.net.URL;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
public class Credentials {
    private String token;
    private URL url;
    private List<CredentialsRole> roles;
}
