package ufsm.csi.cpo.modules.commands;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.tokens.Token;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.DateTime;

import java.net.URL;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReserveNow {
    private URL responseUrl;
    private Token token;
    private DateTime expiryDate;
    private CiString reservationId;
    private CiString locationId;
    private CiString evseUid;
    private CiString authorizationReference;
}
