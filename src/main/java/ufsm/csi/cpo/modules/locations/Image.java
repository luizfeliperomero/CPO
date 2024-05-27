package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.types.CiString;

import java.net.URL;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Image {
    private URL url;
    private URL thumbnail;
    private ImageCategory category;
    private CiString type;
    private int width;
    private int height;
}
