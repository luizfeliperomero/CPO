package ufsm.csi.cpo.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ufsm.csi.cpo.modules.versions.URL;

import java.io.IOException;

public class URLSerializer extends JsonSerializer<URL> {
    @Override
    public void serialize(URL url, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(url.getValue());
    }
}
