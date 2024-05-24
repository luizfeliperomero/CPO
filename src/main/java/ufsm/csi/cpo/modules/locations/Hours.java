package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Hours {
    private boolean twentyfourseven;
    @JsonProperty("regular_hours")
    private List<RegularHours> regularHours;
    @JsonProperty("exceptional_openings")
    private List<ExceptionalPeriod> exceptionalOpenings;
    @JsonProperty("exceptional_closings")
    private List<ExceptionalPeriod> exceptionalClosings;
}
