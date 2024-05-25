package ufsm.csi.cpo.modules.sessions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.sql.Timestamp;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Session {
    private String countryCode;
    private String partyId;
    private String id;
    private Timestamp startDateTime;
    private Timestamp endDateTime;
    private Number kwh;
    private CdrToken cdrToken;
    private AuthMethod authMethod;
    private String authorizationReference;
    private String locationId;
    private String evseUid;
    private String connectorId;
    private String meterId;
    private String currency;
    private List<ChargingPeriod> chargingPeriods;
    private Price totalCost;
    private SessionStatus status;
}
