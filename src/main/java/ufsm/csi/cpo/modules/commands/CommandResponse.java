package ufsm.csi.cpo.modules.commands;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.DisplayText;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommandResponse {
    private CommandResponseType result;
    private int timeout;
    private List<DisplayText> message;
}
