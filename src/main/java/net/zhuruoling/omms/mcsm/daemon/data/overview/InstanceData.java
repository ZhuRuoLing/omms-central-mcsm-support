package net.zhuruoling.omms.mcsm.daemon.data.overview;

public class InstanceData {
    Long running;
    Long total;

    @Override
    public String toString() {
        return "InstanceData{" +
                "running=" + running +
                ", total=" + total +
                '}';
    }

    public Long getRunning() {
        return running;
    }

    public Long getTotal() {
        return total;
    }
}
