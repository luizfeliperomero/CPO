package ufsm.csi.cpo.modules.sessions;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CdrToken {
    private String countryCode;
    private String partyId;
    private String uid;
    private TokenType type;
    private String contractId;
}
