package ufsm.csi.cpo.modules.commands;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.CiString;

import java.net.URL;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UnlockConnector {
    private URL responseUrl;
    private CiString locationId;
    private CiString evseUid;
    private CiString connectorId;
}
