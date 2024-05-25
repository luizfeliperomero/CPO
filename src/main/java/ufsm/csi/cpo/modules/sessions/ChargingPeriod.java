package ufsm.csi.cpo.modules.sessions;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.sql.Timestamp;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChargingPeriod {
    private Timestamp startDateTime;
    private List<CdrDimension> dimensions;
    private String tariffId;
    private String encodingMethod;
    private int encodingMethodVersion;
    private String publicKey;
    private List<SignedValue> signedValues;
    private String url;
}
