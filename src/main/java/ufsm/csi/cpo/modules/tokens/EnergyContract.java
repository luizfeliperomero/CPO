package ufsm.csi.cpo.modules.tokens;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EnergyContract {
    private String supplierName;
    private String contractId;
}
