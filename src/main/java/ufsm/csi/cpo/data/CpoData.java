package ufsm.csi.cpo.data;

import lombok.Data;
import ufsm.csi.cpo.modules.credentials.PlatformInfo;
import ufsm.csi.cpo.modules.types.CiString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CpoData {
    private static CpoData instance;
    private Map<CiString, PlatformInfo> platforms = new HashMap<>();
    private List<String> validCredentialsTokens = new ArrayList<>();
    public static CpoData getInstance() {
        synchronized (CpoData.class) {
            if(instance == null) {
                instance = new CpoData();
            }
        }
        return instance;
    }
}
