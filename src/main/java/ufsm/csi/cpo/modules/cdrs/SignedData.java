package ufsm.csi.cpo.modules.cdrs;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.sessions.SignedValue;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignedData {
    private String encodingMethod;
    private int encodingMethodVersion;
    private String publicKey;
    private List<SignedValue> signedValues;
    private String url;
}
