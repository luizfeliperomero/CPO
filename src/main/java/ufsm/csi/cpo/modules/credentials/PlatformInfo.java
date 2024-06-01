package ufsm.csi.cpo.modules.credentials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufsm.csi.cpo.modules.versions.VersionDetails;
import ufsm.csi.cpo.modules.versions.VersionNumber;

import java.util.Comparator;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlatformInfo {
    private String token;
    private VersionNumber currentVersion;
    private List<VersionDetails> versions;
    private Credentials credentialsUsed;
}
