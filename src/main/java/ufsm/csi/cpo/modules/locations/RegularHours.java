package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegularHours {
    private int weekday;
    @JsonProperty("period_begin")
    private String periodBegin;
    @JsonProperty("period_end")
    private String periodEnd;
}
