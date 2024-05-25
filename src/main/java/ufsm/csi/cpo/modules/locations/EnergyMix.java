package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EnergyMix {
    private boolean isGreenEnergy;
    private List<EnergySource> energySources;
    private List<EnvironmentalImpact> environImpac;
    private String supplierName;
    private String energyProductName;
}
