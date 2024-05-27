package ufsm.csi.cpo.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ufsm.csi.cpo.modules.versions.VersionNumber;

import java.io.IOException;

public class VersionNumberDeserializer extends JsonDeserializer<VersionNumber> {
    @Override
    public VersionNumber deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String versionStr = jsonParser.readValueAs(String.class);
        System.out.println(versionStr);
        switch(versionStr) {
            case "2.2.1":
                return VersionNumber.V2_2_1;
            default:
                System.out.println("Version Not Supported");
        }
        return null;
    }
}
