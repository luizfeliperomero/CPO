package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.versions.URL;

import java.security.Timestamp;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Connector {
    private String id;
    private ConnectorType standard;
    private ConnectorFormat format;
    private PowerType powerType;
    private int maxVoltage;
    private int maxAmperage;
    private int maxElectricPower;
    private List<String> tariffIds;
    private URL termAndConditions;
    private Timestamp lastUpdated;
}
