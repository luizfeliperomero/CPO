package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class StatusSchedule {
    @JsonProperty("period_begin")
    private Timestamp periodBegin;
    @JsonProperty("period_end")
    private Timestamp periodEnd;
    private Status status;
}
