package ufsm.csi.cpo.modules.hubclientinfo;

import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.Role;

import java.sql.Timestamp;

public class ClientInfo {
    private CiString partyId;
    private CiString countryCode;
    private Role role;
    private ConnectionStatus status;
    private Timestamp lastUpdated;
}
