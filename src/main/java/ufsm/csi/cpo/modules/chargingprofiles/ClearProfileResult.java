package ufsm.csi.cpo.modules.chargingprofiles;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClearProfileResult {
    private ChargingProfileResultType result;
}
