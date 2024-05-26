package ufsm.csi.cpo.modules.sessions;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.CiString;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignedValue {
    private CiString nature;
    private String plainData;
    private String signedData;
}
