package net.zhuruoling.omms.mcsm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Util {
    public static Gson gson = new GsonBuilder().serializeNulls().create();
}
