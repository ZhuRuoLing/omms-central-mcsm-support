package net.zhuruoling.omms.mcsm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.regex.Pattern;

public class Util {
    public static Gson gson = new GsonBuilder().serializeNulls().create();

    public static Gson prettyPrintingGson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public static Pattern dockerPortParsePattern = Pattern.compile("([0-9]+):([0-9]+)/([A-Za-z0-9]+)");
}
