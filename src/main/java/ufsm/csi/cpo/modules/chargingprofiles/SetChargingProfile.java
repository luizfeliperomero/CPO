package ufsm.csi.cpo.modules.chargingprofiles;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.versions.URL;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SetChargingProfile {
    private ChargingProfile chargingProfile;
    private URL responseUrl;
}
