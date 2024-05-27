package ufsm.csi.cpo.modules.versions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endpoint {
    private ModuleID identifier;
    private InterfaceRole role;
    private URL url;
}
