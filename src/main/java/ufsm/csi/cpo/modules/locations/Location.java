package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.DateTime;
import ufsm.csi.cpo.modules.types.DisplayText;

import java.sql.Timestamp;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Location {
    private CiString countryCode;
    private CiString partyId;
    private CiString id;
    private boolean publish;
    private List<PublishTokenType> publishAllowedTo;
    private String name;
    private String address;
    private String city;
    private String postalCode;
    private String state;
    private String country;
    private GeoLocation coordinates;
    private List<AdditionalGeoLocation> relatedLocations;
    private ParkingType parkingType;
    private List<EVSE> evses;
    private List<DisplayText> directions;
    private BusinessDetails operator;
    private BusinessDetails suboperator;
    private BusinessDetails owner;
    private List<Facility> facilities;
    private String timeZone;
    private Hours openingTimes;
    private boolean chargingWhenClosed;
    private List<Image> images;
    private EnergyMix energyMix;
    private DateTime lastUpdated;
}
