package net.zhuruoling.omms.mcsm.daemon;

import net.zhuruoling.omms.mcsm.daemon.data.instance.InstanceData;

public class MCSMDaemonInstance {
    private final String displayName;
    private final DaemonConnector daemonConnector;
    private final InstanceData instanceData;

    public MCSMDaemonInstance(String displayName, DaemonConnector daemonConnector, InstanceData instanceData) {
        this.displayName = displayName;
        this.daemonConnector = daemonConnector;
        this.instanceData = instanceData;
    }

    public String getDisplayName() {
        return displayName;
    }

    public DaemonConnector getDaemonConnector() {
        return daemonConnector;
    }

    public InstanceData getInstanceData() {
        return instanceData;
    }
}
