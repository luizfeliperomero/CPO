package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("party_id")
    private String partyId;
    private String id;
    private boolean publish;
    @JsonProperty("publish_allowed_to")
    private List<PublishTokenType> publishAllowedTo;
    private String name;
    private String address;
    private String city;
    @JsonProperty("postal_code")
    private String postalCode;
    private String state;
    private String country;
    private GeoLocation coordinates;
    @JsonProperty("related_locations")
    private List<AdditionalGeoLocation> relatedLocations;
    @JsonProperty("parking_type")
    private ParkingType parkingType;
    private List<EVSE> evses;
    private List<DisplayText> directions;
    private BusinessDetails operator;
    private BusinessDetails suboperator;
    private BusinessDetails owner;
    private List<Facility> facilities;
    @JsonProperty("time_zone")
    private String timeZone;
    @JsonProperty("opening_times")
    private Hours openingTimes;
    @JsonProperty("charging_when_closed")
    private boolean chargingWhenClosed;
    private List<Image> images;
    @JsonProperty("energy_mix")
    private EnergyMix energyMix;
    @JsonProperty("last_updated")
    private Timestamp lastUpdated;
}
