package net.zhuruoling.omms.mcsm.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.zhuruoling.omms.central.util.Util;
import net.zhuruoling.omms.mcsm.daemon.DaemonConnector;
import net.zhuruoling.omms.mcsm.daemon.MCSMDaemon;
import net.zhuruoling.omms.mcsm.exception.PluginInitializationException;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class Config {

    public final static Config INSTANCE = new Config();
    public final static Path CONFIG_PATH = Path.of(Util.joinFilePaths("config", "mcsm-omms.json"));
    public final static Path CONFIG_DIR = Path.of(Util.joinFilePaths("config"));
    private final static Gson gson = new GsonBuilder().serializeNulls().create();

    private ConfigStorage configStorage;

    public final Map<MCSMDaemon, DaemonConnector> daemonConnectorMap = new HashMap<>();

    private Config() {
    }


    public boolean readConfig() {
        boolean init = false;
        if (!CONFIG_DIR.toFile().exists()) {
            init = true;
            try {
                CONFIG_DIR.toFile().mkdir();
            } catch (SecurityException e) {
                throw new PluginInitializationException("Cannot mkdir %s".formatted(CONFIG_DIR.toString()), e);
            }
        }
        if (!CONFIG_PATH.toFile().exists()) {
            init = true;
            try {
                CONFIG_PATH.toFile().createNewFile();
                var file = CONFIG_PATH.toFile();
                var writer = new BufferedWriter(new FileWriter(file));
                gson.toJson(new ConfigStorage(), writer);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new PluginInitializationException("Cannot create plugin config.", e);
            }
        }
        try (var reader = new BufferedReader(new FileReader(CONFIG_PATH.toFile()));) {
            configStorage = gson.fromJson(reader, ConfigStorage.class);
        } catch (IOException e) {
            throw new PluginInitializationException("Cannot read plugin config.", e);
        }
        return init;
    }

    public List<MCSMDaemon> getMcsmDaemons() {
        return configStorage.mcsmDaemons;
    }

    public Map<MCSMDaemon, DaemonConnector> getDaemonConnectorMap() {
        return daemonConnectorMap;
    }

    public void run(Consumer<Map<MCSMDaemon, DaemonConnector>> func) {
        func.accept(daemonConnectorMap);
    }

    public String getFrpcDefaultConfigPath() {
        return configStorage.frpcDefaultConfigPath;
    }

    public boolean isFrpcAutoConfigureEnabled(){
        return configStorage.frpcAutoConfigure;
    }

    private class ConfigStorage {
        private List<MCSMDaemon> mcsmDaemons = new ArrayList<>();
        private String frpcDefaultConfigPath = "";
        private boolean frpcAutoConfigure = false;
    }
}
