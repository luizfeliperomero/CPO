package ufsm.csi.cpo.modules.sessions;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.cdrs.CdrToken;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.DateTime;

import java.sql.Timestamp;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Session {
    private CiString countryCode;
    private CiString partyId;
    private CiString id;
    private DateTime startDateTime;
    private DateTime endDateTime;
    private Number kwh;
    private CdrToken cdrToken;
    private AuthMethod authMethod;
    private CiString authorizationReference;
    private CiString locationId;
    private CiString evseUid;
    private CiString connectorId;
    private String meterId;
    private String currency;
    private List<ChargingPeriod> chargingPeriods;
    private Price totalCost;
    private SessionStatus status;
}
