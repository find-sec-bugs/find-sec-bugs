package testcode.sqli.turbine;

import org.apache.torque.util.BasePeer;

public class TorqueSql {

    public void injection1(BasePeer peer, String injection) {
        peer.executeQuery(injection);
        peer.executeQuery(injection,false,null);
        peer.executeQuery(injection,0,0,false,null);
        peer.executeQuery(injection,0,0,"",false);
        peer.executeQuery(injection,"");
        peer.executeQuery(injection,"",false);
    }

    public void injection2(String injection) {
        BasePeer.executeQuery(injection);
        BasePeer.executeQuery(injection,false,null);
        BasePeer.executeQuery(injection,0,0,false,null);
        BasePeer.executeQuery(injection,0,0,"",false);
        BasePeer.executeQuery(injection,"");
        BasePeer.executeQuery(injection,"",false);
    }

    public void falsePositive(BasePeer peer) {
        String constantValue = "SELECT * FROM test";

        peer.executeQuery(constantValue);
        peer.executeQuery(constantValue,false,null);
        peer.executeQuery(constantValue,0,0,false,null);
        peer.executeQuery(constantValue,0,0,"",false);
        peer.executeQuery(constantValue,"");
        peer.executeQuery(constantValue,"",false);
    }
}
