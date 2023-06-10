package net.zhuruoling.omms.mcsm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.zhuruoling.omms.central.util.Util;
import net.zhuruoling.omms.mcsm.daemon.MCSMDaemon;
import net.zhuruoling.omms.mcsm.exception.PluginInitializationException;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class Config {

    public final static Config INSTANCE = new Config();
    public final static Path CONFIG_PATH = Path.of(Util.joinFilePaths("config", "mcsm-omms.json"));
    public final static Path CONFIG_DIR = Path.of(Util.joinFilePaths("config"));
    private final static Gson gson = new GsonBuilder().serializeNulls().create();

    private List<MCSMDaemon> mcsmDaemons;

    private Config() {
    }


    public void readConfig() {
        mcsmDaemons.clear();
        if (!CONFIG_DIR.toFile().exists()) {
            try {
                CONFIG_DIR.toFile().mkdir();
            } catch (SecurityException e) {
                throw new PluginInitializationException("Cannot mkdir %s".formatted(CONFIG_DIR.toString()), e);
            }
        }
        if (!CONFIG_PATH.toFile().exists()) {
            try {
                CONFIG_PATH.toFile().createNewFile();
                var file = CONFIG_PATH.toFile();
                var writer = new BufferedWriter(new FileWriter(file));
                gson.toJson(new ArrayList<MCSMDaemon>(), writer);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new PluginInitializationException("Cannot create plugin config.", e);
            }
        }
        try (var reader = new BufferedReader(new FileReader(CONFIG_PATH.toFile()));){
           mcsmDaemons.addAll(gson.fromJson(reader, mcsmDaemons.getClass()));
        } catch (IOException e) {
            throw new PluginInitializationException("Cannot read plugin config.", e);
        }
    }

    public List<MCSMDaemon> getMcsmDaemons() {
        return mcsmDaemons;
    }
}
