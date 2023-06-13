package net.zhuruoling.omms.mcsm;

import net.zhuruoling.omms.mcsm.daemon.DaemonConnector;
import net.zhuruoling.omms.mcsm.daemon.MCSMDaemon;

public class DaemonTest {
    public static void main(String[] args) {
        var token1 = "da2e3138f21040d8797f46f6ae44146b3324f1a85a8c7be";
        var token2 = "f047ea3adf584d77d833821bd1f4030a56f0cec0eaea23f";
        var daemon1 = new MCSMDaemon(token1,"localhost:24444","testDaemon");
        DaemonConnector connector1 = new DaemonConnector(daemon1);
        connector1.connect();
        System.out.println(connector1.fetchInfo());
    }
}
