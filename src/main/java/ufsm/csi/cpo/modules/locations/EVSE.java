package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;

public class EVSE {
    private String uid;
    @JsonProperty("evse_id")
    private String evseId;
    private Status status;
    @JsonProperty("status_schedule")
    private List<StatusSchedule> statusSchedule;
    private List<Capability> capabilities;
    private List<Connector> connectors;
    @JsonProperty("floor_level")
    private String floorLevel;
    private GeoLocation coordinates;
    @JsonProperty("physical_reference")
    private String physicalReference;
    private List<DisplayText> directions;
    @JsonProperty("parking_restrictions")
    private List<ParkingRestriction> parkingRestrictions;
    private List<Image> images;
    @JsonProperty("last_updated")
    private Timestamp lastUpdated;

}
