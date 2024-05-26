package ufsm.csi.cpo.modules.cdrs;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.tokens.TokenType;
import ufsm.csi.cpo.modules.types.CiString;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CdrToken {
    private CiString countryCode;
    private CiString partyId;
    private CiString uid;
    private TokenType type;
    private CiString contractId;
}
