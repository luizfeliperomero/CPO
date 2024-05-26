package ufsm.csi.cpo.modules.sessions;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.cdrs.CdrDimension;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.DateTime;

import java.sql.Timestamp;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChargingPeriod {
    private DateTime startDateTime;
    private List<CdrDimension> dimensions;
    private CiString tariffId;
    private CiString encodingMethod;
    private int encodingMethodVersion;
    private String publicKey;
    private List<SignedValue> signedValues;
    private String url;
}
