package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EnergyMix {
    @JsonProperty("is_green_energy")
    private boolean isGreenEnergy;
    @JsonProperty("energy_sources")
    private List<EnergySource> energySources;
    @JsonProperty("environ_impac")
    private List<EnvironmentalImpact> environImpac;
    @JsonProperty("supplier_name")
    private String supplierName;
    @JsonProperty("energy_product_name")
    private String energyProductName;
}
