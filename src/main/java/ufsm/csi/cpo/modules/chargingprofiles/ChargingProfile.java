package ufsm.csi.cpo.modules.chargingprofiles;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.DateTime;

import java.sql.Timestamp;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChargingProfile {
    private DateTime startDateTime;
    private int duration;
    private ChargingRateUnit chargingRateUnit;
    private Number minChargingRate;
    private List<ChargingProfilePeriod> chargingProfilePeriod;
}
