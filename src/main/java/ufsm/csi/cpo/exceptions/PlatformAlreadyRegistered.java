package ufsm.csi.cpo.exceptions;


import ufsm.csi.cpo.modules.types.CiString;

public class PlatformAlreadyRegistered extends Exception {

    public PlatformAlreadyRegistered() {
        super("The platform has already been registered");
    }
}
