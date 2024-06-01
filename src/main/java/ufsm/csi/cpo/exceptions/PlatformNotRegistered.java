package ufsm.csi.cpo.exceptions;

import ufsm.csi.cpo.modules.types.CiString;

public class PlatformNotRegistered extends Exception{
    public PlatformNotRegistered() {
    super("The platform can't access the server credentials object because it's not registered to the server's system");
    }
}
