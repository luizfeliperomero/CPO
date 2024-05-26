package ufsm.csi.cpo.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ufsm.csi.cpo.modules.types.CiString;

import java.io.IOException;

public class CiStringSerializer extends JsonSerializer<CiString> {
    @Override
    public void serialize(CiString ciString, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(ciString.getValue());
    }
}
