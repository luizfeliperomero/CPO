package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.versions.URL;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BusinessDetails {
    private String name;
    private URL website;
    private Image logo;
}
