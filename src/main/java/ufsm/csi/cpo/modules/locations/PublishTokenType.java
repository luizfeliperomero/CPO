package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PublishTokenType {
    private String uid;
    private TokenType type;
    @JsonProperty("visual_number")
    private String visualNumber;
    private String issuer;
    @JsonProperty("groud_id")
    private String groupId;
}
