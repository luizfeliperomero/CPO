package ufsm.csi.cpo.modules.versions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ufsm.csi.cpo.serialization.VersionNumberSerializer;

@JsonSerialize(using = VersionNumberSerializer.class)
public enum VersionNumber {
    V2_0,
    V2_1,
    V2_1_1,
    V2_2,
    V2_2_1;

    @Override
    public String toString() {
        switch (this) {
            case V2_0:
                return "2.0";
            case V2_1:
                return "2.1";
            case V2_1_1:
                return "2.1.1";
            case V2_2:
                return "2.2";
            case V2_2_1:
                return "2.2.1";
            default:
                return super.toString();
        }
    }
}
