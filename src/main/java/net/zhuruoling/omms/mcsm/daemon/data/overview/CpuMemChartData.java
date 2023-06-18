package net.zhuruoling.omms.mcsm.daemon.data.overview;

public class CpuMemChartData {
    int mem;
    int cpu;


    @Override
    public String toString() {
        return "CpuMemChartData{" +
                "mem=" + mem +
                ", cpu=" + cpu +
                '}';
    }

    public int getMem() {
        return mem;
    }

    public int getCpu() {
        return cpu;
    }
}
