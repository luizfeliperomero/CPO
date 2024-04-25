package ufsm.csi.cpo.modules.versions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionDetails {
    VersionNumber version;
    List<Endpoint> endpoints;
}
