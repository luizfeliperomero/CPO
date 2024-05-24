package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;
import ufsm.csi.cpo.modules.versions.URL;

import java.security.Timestamp;
import java.util.List;

public class Connector {
    private String id;
    private ConnectorType standard;
    private ConnectorFormat format;
    @JsonProperty("power_type")
    private PowerType powerType;
    @JsonProperty("max_voltage")
    private int maxVoltage;
    @JsonProperty("max_amperage")
    private int maxAmperage;
    @JsonProperty("max_electric_power")
    private int maxElectricPower;
    @JsonProperty("tariff_ids")
    private List<String> tariffIds;
    @JsonProperty("term_and_conditions")
    private URL termAndConditions;
    @JsonProperty("last_updated")
    private Timestamp lastUpdated;
}
