package ufsm.csi.cpo.modules.cdrs;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.sessions.AuthMethod;
import ufsm.csi.cpo.modules.sessions.ChargingPeriod;
import ufsm.csi.cpo.modules.sessions.Price;
import ufsm.csi.cpo.modules.tariffs.Tariff;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.DateTime;

import java.sql.Timestamp;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CDR {
    private CiString countryCode;
    private CiString partyId;
    private CiString id;
    private DateTime startDateTime;
    private DateTime endDateTime;
    private CiString sessionId;
    private CdrToken cdrToken;
    private AuthMethod authMethod;
    private CiString authorizationReference;
    private CdrLocation cdrLocation;
    private String meterId;
    private String currency;
    private List<Tariff> tariffs;
    private List<ChargingPeriod> chargingPeriods;
    private SignedData signedData;
    private Price totalCost;
    private Price totalFixedCost;
    private Number totalEnergy;
    private Price totalEnergyCost;
    private Number totalTime;
    private Price totalTimeCost;
    private Number totalParkingTime;
    private Price totalParkingCost;
    private Price totalReservationCost;
    private String remark;
    private CiString invoiceReferenceId;
    private boolean credit;
    private CiString creditReferenceId;
    private boolean homeChargingCompensation;
    private DateTime lastUpdated;
}
