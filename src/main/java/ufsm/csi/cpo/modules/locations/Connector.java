package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.DateTime;
import ufsm.csi.cpo.modules.versions.URL;

import java.security.Timestamp;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Connector {
    private CiString id;
    private ConnectorType standard;
    private ConnectorFormat format;
    private PowerType powerType;
    private int maxVoltage;
    private int maxAmperage;
    private int maxElectricPower;
    private List<CiString> tariffIds;
    private URL termAndConditions;
    private DateTime lastUpdated;
}
