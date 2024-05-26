package ufsm.csi.cpo.modules.chargingprofiles;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.DateTime;

import java.sql.Timestamp;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ActiveChargingProfile {
    private DateTime startDateTime;
    private ChargingProfile chargingProfile;
}
