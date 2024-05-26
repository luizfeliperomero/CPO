package ufsm.csi.cpo.modules.credentials;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufsm.csi.cpo.modules.locations.BusinessDetails;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.Role;
import ufsm.csi.cpo.modules.versions.URL;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CredentialsRole {
    private Role role;
    private BusinessDetails businessDetails;
    private CiString partyId;
    private CiString countryCode;
}
