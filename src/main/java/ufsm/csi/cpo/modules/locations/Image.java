package ufsm.csi.cpo.modules.locations;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.versions.URL;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Image {
    private URL url;
    private URL thumbnail;
    private ImageCategory category;
    private String type;
    private int width;
    private int height;
}
