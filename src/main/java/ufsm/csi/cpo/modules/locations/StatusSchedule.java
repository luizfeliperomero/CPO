package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.DateTime;

import java.sql.Timestamp;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StatusSchedule {
    private DateTime periodBegin;
    private DateTime periodEnd;
    private Status status;
}
