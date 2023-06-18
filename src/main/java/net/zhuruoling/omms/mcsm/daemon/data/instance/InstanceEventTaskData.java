package net.zhuruoling.omms.mcsm.daemon.data.instance;

public class InstanceEventTaskData {
    boolean autoRestart;
    boolean ignore;
    boolean autoStart;

    public boolean isAutoRestart() {
        return autoRestart;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public boolean isAutoStart() {
        return autoStart;
    }
}
