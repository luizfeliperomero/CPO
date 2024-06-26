package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.DateTime;
import ufsm.csi.cpo.modules.types.DisplayText;

import java.sql.Timestamp;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EVSE {
    private CiString uid;
    private CiString evseId;
    private Status status;
    private List<StatusSchedule> statusSchedule;
    private List<Capability> capabilities;
    private List<Connector> connectors;
    private String floorLevel;
    private GeoLocation coordinates;
    private String physicalReference;
    private List<DisplayText> directions;
    private List<ParkingRestriction> parkingRestrictions;
    private List<Image> images;
    private DateTime lastUpdated;

}
