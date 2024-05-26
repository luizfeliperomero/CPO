package ufsm.csi.cpo.modules.commands;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.versions.URL;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CancelReservation {
    private URL responseUrl;
    private CiString reservationId;
}
