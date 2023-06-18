package net.zhuruoling.omms.mcsm.daemon.data.overview;

import net.zhuruoling.omms.mcsm.Util;
import org.json.JSONObject;

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


}
