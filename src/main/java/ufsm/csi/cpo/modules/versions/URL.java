package ufsm.csi.cpo.modules.versions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufsm.csi.cpo.serialization.URLSerializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = URLSerializer.class)
public class URL {
    private String value;
}
