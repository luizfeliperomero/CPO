package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.DisplayText;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdditionalGeoLocation {
    private String latitude;
    private String longitude;
    private DisplayText name;
}
