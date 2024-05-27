package ufsm.csi.cpo.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ufsm.csi.cpo.modules.types.CiString;

import java.io.IOException;

public class CiStringDeserializer extends JsonDeserializer<CiString> {
    @Override
    public CiString deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();
        return new CiString(value);
    }
}
