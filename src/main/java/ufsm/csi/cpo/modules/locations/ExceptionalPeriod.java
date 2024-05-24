package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.Timestamp;

public class ExceptionalPeriod {
    @JsonProperty("period_begin")
    private Timestamp periodBegin;
    @JsonProperty("period_end")
    private Timestamp periodEnd;
}
