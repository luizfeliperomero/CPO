package ufsm.csi.cpo.modules.versions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Version {
    private VersionNumber version;
    private URL url;
}
