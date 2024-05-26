package ufsm.csi.cpo.modules.cdrs;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.tariffs.TariffDimensionType;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceComponent {
    private TariffDimensionType type;
    private Number price;
    private Number vat;
    private int stepSize;
}
