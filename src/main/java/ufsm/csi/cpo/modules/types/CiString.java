package ufsm.csi.cpo.modules.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

@Data
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
}
