package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.CiString;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PublishTokenType {
    private CiString uid;
    private TokenType type;
    private String visualNumber;
    private String issuer;
    private CiString groupId;
}
