package ufsm.csi.cpo.modules.versions;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ufsm.csi.cpo.serialization.VersionNumberDeserializer;
import ufsm.csi.cpo.serialization.VersionNumberSerializer;

import java.util.*;

@JsonSerialize(using = VersionNumberSerializer.class)
@JsonDeserialize(using = VersionNumberDeserializer.class)
public enum VersionNumber {
    V2_0,
    V2_1,
    V2_1_1,
    V2_2,
    V2_2_1;


    @Override
    public String toString() {
        switch (this) {
            case V2_0:
                return "2.0";
            case V2_1:
                return "2.1";
            case V2_1_1:
                return "2.1.1";
            case V2_2:
                return "2.2";
            case V2_2_1:
                return "2.2.1";
            default:
                return super.toString();
        }
    }

    public static VersionNumber min() {
       return VersionNumber.V2_0;
    }

    public static VersionNumber max() {
        return VersionNumber.V2_2_1;
    }

    public static boolean isGreater(VersionNumber lhs, VersionNumber rhs) {
        String lhsStr = lhs.toString();
        String rhsStr = rhs.toString();
        List<String> lhsStrSplitedByComma = Arrays.asList(lhsStr.split("\\."));
        List<String> rhsStrSplitedByComma = Arrays.asList(rhsStr.split("\\."));
        Deque<Integer> deque = new ArrayDeque<>();
        for(String str : lhsStrSplitedByComma) {
            int lhsValue = Integer.parseInt(str);
            deque.addLast(lhsValue);
        }
        for(String str : rhsStrSplitedByComma) {
            int rhsValue = Integer.parseInt(str);
            if(rhsValue > deque.peek()) {
                return false;
            } else if(rhsValue < deque.peek()){
                return true;
            }
            deque.pop();
        }
        return !deque.isEmpty();
    }

}
