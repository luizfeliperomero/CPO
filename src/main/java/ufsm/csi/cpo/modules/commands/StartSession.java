package ufsm.csi.cpo.modules.commands;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.tokens.Token;
import ufsm.csi.cpo.modules.types.CiString;

import java.net.URL;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StartSession {
    private URL responseUrl;
    private Token token;
    private CiString locationId;
    private CiString evseUid;
    private CiString connectorId;
    private CiString authorizationReference;
}
