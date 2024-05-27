package ufsm.csi.cpo.modules.tariffs;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.DateTime;
import ufsm.csi.cpo.modules.types.DisplayText;
import ufsm.csi.cpo.modules.locations.EnergyMix;
import ufsm.csi.cpo.modules.sessions.Price;

import java.net.URL;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Tariff {
    private CiString countryCode;
    private CiString partyId;
    private CiString id;
    private String currency;
    private TariffType type;
    private List<DisplayText> tariffAltText;
    private URL tariffAltUrl;
    private Price minPrice;
    private Price maxPrice;
    private List<TariffElement> elements;
    private DateTime startDateTime;
    private DateTime endDateTime;
    private EnergyMix energyMix;
    private DateTime lastUpdated;
}
