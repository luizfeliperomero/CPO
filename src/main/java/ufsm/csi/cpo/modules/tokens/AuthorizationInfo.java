package ufsm.csi.cpo.modules.tokens;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.locations.LocationReferences;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.DisplayText;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthorizationInfo {
    private AllowedType allowed;
    private Token token;
    private LocationReferences location;
    private CiString authorizationReference;
    private DisplayText info;
}
