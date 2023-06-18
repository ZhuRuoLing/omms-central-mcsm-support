package net.zhuruoling.omms.mcsm.daemon.data.overview;

import java.util.Arrays;

public class SystemData {
    @Override
    public String toString() {
        return "SystemData{" +
                "cpuUsage=" + cpuUsage +
                ", release='" + release + '\'' +
                ", totalmem=" + totalmem +
                ", freemem=" + freemem +
                ", type='" + type + '\'' +
                ", platform='" + platform + '\'' +
                ", uptime=" + uptime +
                ", processMem=" + processMem +
                ", processCpu=" + processCpu +
                ", cwd='" + cwd + '\'' +
                ", hostname='" + hostname + '\'' +
                ", memUsage='" + memUsage + '\'' +
                ", loadavg=" + Arrays.toString(loadavg) +
                '}';
    }

    double cpuUsage;
    String release;
    Long totalmem;
    Long freemem;
    String type;
    String platform;
    double uptime;
    long processMem;
    long processCpu;
    String cwd;
    String hostname;
    String memUsage;
    double[] loadavg;

    public double getCpuUsage() {
        return cpuUsage;
    }

    public String getRelease() {
        return release;
    }

    public Long getTotalmem() {
        return totalmem;
    }

    public Long getFreemem() {
        return freemem;
    }

    public String getType() {
        return type;
    }

    public String getPlatform() {
        return platform;
    }

    public double getUptime() {
        return uptime;
    }

    public long getProcessMem() {
        return processMem;
    }

    public long getProcessCpu() {
        return processCpu;
    }

    public String getCwd() {
        return cwd;
    }

    public String getHostname() {
        return hostname;
    }

    public String getMemUsage() {
        return memUsage;
    }

    public double[] getLoadavg() {
        return loadavg;
    }
}
