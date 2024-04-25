package ufsm.csi.cpo.modules.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufsm.csi.cpo.modules.versions.URL;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsRole {
    private String token;
    private List<Role> roles;
    private URL url;
}
