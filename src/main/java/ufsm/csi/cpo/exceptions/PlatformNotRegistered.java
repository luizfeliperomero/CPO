package ufsm.csi.cpo.exceptions;

import ufsm.csi.cpo.modules.types.CiString;

public class PlatformNotRegistered extends Exception{
    public PlatformNotRegistered() {
    super("The platform it's not registered to the server's system");
    }
}
