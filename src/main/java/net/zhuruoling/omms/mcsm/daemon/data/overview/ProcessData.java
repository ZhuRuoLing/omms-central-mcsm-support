package net.zhuruoling.omms.mcsm.daemon.data.overview;

public class ProcessData {
    String cwd;
    Long memory;
    Long cpu;

    @Override
    public String toString() {
        return "ProcessData{" +
                "cwd='" + cwd + '\'' +
                ", memory=" + memory +
                ", cpu=" + cpu +
                '}';
    }

    public String getCwd() {
        return cwd;
    }

    public Long getMemory() {
        return memory;
    }

    public Long getCpu() {
        return cpu;
    }
}
