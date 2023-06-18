package net.zhuruoling.omms.mcsm.daemon.data.instance;

public class InstanceData {//IInstanceDetail
    String instanceUuid;
    int started;
    int status;
    InstanceConfigData config;
    InstanceInfoData info;


    public String getInstanceUuid() {
        return instanceUuid;
    }

    public int getStarted() {
        return started;
    }

    public int getStatus() {
        return status;
    }

    public InstanceConfigData getConfig() {
        return config;
    }

    public InstanceInfoData getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "InstanceData{" +
                "instanceUuid='" + instanceUuid + '\'' +
                ", started=" + started +
                ", status=" + status +
                ", config=" + config +
                ", info=" + info +
                '}';
    }
}
