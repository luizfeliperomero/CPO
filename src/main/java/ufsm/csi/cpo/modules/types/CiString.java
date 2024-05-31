package ufsm.csi.cpo.modules.types;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import ufsm.csi.cpo.serialization.CiStringDeserializer;
import ufsm.csi.cpo.serialization.CiStringSerializer;

@Data
@JsonSerialize(using = CiStringSerializer.class)
@JsonDeserialize(using = CiStringDeserializer.class)
public class CiString {
    private final String value;
    public CiString(String value) {
        if(!isValidAscii(value)) {
            throw new IllegalArgumentException("String contains non-printable or non-ASCII characters.");
        }
        this.value = value;
    }

    private boolean isValidAscii(String value) {
        for (char c : value.toCharArray()) {
            if (c < 32 || c > 126) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CiString that = (CiString ) obj;
        return value.equalsIgnoreCase(that.value);
    }

    @Override
    public int hashCode() {
        return this.value.toLowerCase().hashCode();
    }
}
