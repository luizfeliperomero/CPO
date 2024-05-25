package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Hours {
    private boolean twentyfourseven;
    private List<RegularHours> regularHours;
    private List<ExceptionalPeriod> exceptionalOpenings;
    private List<ExceptionalPeriod> exceptionalClosings;
}
