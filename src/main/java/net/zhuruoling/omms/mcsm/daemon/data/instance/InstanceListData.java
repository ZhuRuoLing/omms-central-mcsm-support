package net.zhuruoling.omms.mcsm.daemon.data.instance;

import net.zhuruoling.omms.mcsm.Util;
import org.json.JSONObject;

import java.util.List;

public class InstanceListData {
    List<InstanceData> data;
    long pageSize;
    long page;
    long maxPage;

    public static InstanceListData fromJson(JSONObject jsonObject){
        String json = jsonObject.toString();
        return Util.gson.fromJson(json, InstanceListData.class);
    }

    public List<InstanceData> getData() {
        return data;
    }

    public long getPageSize() {
        return pageSize;
    }

    public long getPage() {
        return page;
    }

    public long getMaxPage() {
        return maxPage;
    }

    @Override
    public String toString() {
        return "InstanceListData{" +
                "data=" + data +
                ", pageSize=" + pageSize +
                ", page=" + page +
                ", maxPage=" + maxPage +
                '}';
    }
}
