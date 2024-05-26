package ufsm.csi.cpo.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ufsm.csi.cpo.modules.types.DateTime;

import java.io.IOException;

public class DateTimeSerializer extends JsonSerializer<DateTime> {
    @Override
    public void serialize(DateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(dateTime.getDateTime());
    }
}
