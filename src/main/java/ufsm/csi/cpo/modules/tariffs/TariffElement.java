package ufsm.csi.cpo.modules.tariffs;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.cdrs.PriceComponent;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TariffElement {
    private List<PriceComponent> priceComponents;
    private TariffRestrictions restrictions;
}
