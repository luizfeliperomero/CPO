package ufsm.csi.cpo.modules.cdrs;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.locations.ConnectorFormat;
import ufsm.csi.cpo.modules.locations.ConnectorType;
import ufsm.csi.cpo.modules.locations.GeoLocation;
import ufsm.csi.cpo.modules.locations.PowerType;
import ufsm.csi.cpo.modules.types.CiString;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CdrLocation {
    private CiString id;
    private String name;
    private String address;
    private String city;
    private String postalCode;
    private String state;
    private String country;
    private GeoLocation coordinates;
    private CiString evseUid;
    private CiString evseId;
    private CiString connectorId;
    private ConnectorType connectorStandard;
    private ConnectorFormat connectorFormat;
    private PowerType connectorPowerType;
}
