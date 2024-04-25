package ufsm.csi.cpo.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ufsm.csi.cpo.modules.versions.VersionNumber;

import java.io.IOException;

public class VersionNumberSerializer extends JsonSerializer<VersionNumber> {
    @Override
    public void serialize(VersionNumber versionNumber, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(versionNumber.toString());
    }
}
