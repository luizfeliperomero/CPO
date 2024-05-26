package ufsm.csi.cpo.modules.tokens;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.locations.TokenType;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.DateTime;

import java.sql.Timestamp;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Token {
    private CiString countryCode;
    private CiString partyId;
    private CiString uid;
    private TokenType type;
    private CiString contractId;
    private String visualNumber;
    private String issuer;
    private CiString groupId;
    private boolean valid;
    private WhitelistType whitelist;
    private String language;
    private ProfileType defaultProfileType;
    private EnergyContract energyContract;
    private DateTime lastUpdated;
}
