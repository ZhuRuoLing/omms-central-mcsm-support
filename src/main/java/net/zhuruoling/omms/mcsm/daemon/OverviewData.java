package net.zhuruoling.omms.mcsm.daemon;

import com.google.gson.Gson;
import net.zhuruoling.omms.mcsm.Util;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("all")
public class OverviewData {

    ProcessData process;
    InstanceData instance;
    SystemData system;

    List<CpuMemChartData> cpuMemChart;
    String version;

    @Override
    public String toString() {
        return "OverviewData{" +
                "process=" + process +
                ", instance=" + instance +
                ", system=" + system +
                ", cpuMemChart=" + cpuMemChart +
                ", version='" + version + '\'' +
                '}';
    }

    public static OverviewData fromJson(JSONObject jsonObject){
        String json = jsonObject.toString();
        return Util.gson.fromJson(json, OverviewData.class);
    }


    public static class ProcessData{
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

    public static class InstanceData{
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

    public static class SystemData{
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

    public static class CpuMemChartData{
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
}
