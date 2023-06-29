package net.zhuruoling.omms.mcsm.daemon.data.instance;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InstanceDockerConfigData {//IDockerConfig
    String containerName;
    String image;
    double memory;
    List<String> ports;
    List<String> extraVolumes;
    double maxSpace;
    @Nullable
    Long network;
    double io;
    String networkMode;
    List<String> networkAliases;
    String cpusetCpus;
    double cpuUsage;

    public String getContainerName() {
        return containerName;
    }

    public String getImage() {
        return image;
    }

    public double getMemory() {
        return memory;
    }

    public List<String> getPorts() {
        return ports;
    }

    public List<String> getExtraVolumes() {
        return extraVolumes;
    }

    public double getMaxSpace() {
        return maxSpace;
    }

    public Long getNetwork() {
        return network;
    }

    public double getIo() {
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

    public double getCpuUsage() {
        return cpuUsage;
    }
}
