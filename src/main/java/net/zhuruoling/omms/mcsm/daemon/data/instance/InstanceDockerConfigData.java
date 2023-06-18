package net.zhuruoling.omms.mcsm.daemon.data.instance;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InstanceDockerConfigData {//IDockerConfig
    String containerName;
    String image;
    String memory;
    List<String> ports;
    List<String> extraVolumes;
    long maxSpace;
    @Nullable
    Long network;
    long io;
    String networkMode;
    List<String> networkAliases;
    String cpusetCpus;
    long cpuUsage;

    public String getContainerName() {
        return containerName;
    }

    public String getImage() {
        return image;
    }

    public String getMemory() {
        return memory;
    }

    public List<String> getPorts() {
        return ports;
    }

    public List<String> getExtraVolumes() {
        return extraVolumes;
    }

    public long getMaxSpace() {
        return maxSpace;
    }

    public Long getNetwork() {
        return network;
    }

    public long getIo() {
        return io;
    }

    public String getNetworkMode() {
        return networkMode;
    }

    public List<String> getNetworkAliases() {
        return networkAliases;
    }

    public String getCpusetCpus() {
        return cpusetCpus;
    }

    public long getCpuUsage() {
        return cpuUsage;
    }
}
