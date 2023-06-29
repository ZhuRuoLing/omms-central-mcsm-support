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

    public String getFrpcClientCommonConfig() {
        return configStorage.frpcClientCommonConfig;
    }

    public String getFrpcClientPortConfig() {
        return configStorage.frpcClientPortConfig;
    }

    public String getFrpcClientExecutablePath(){
        return configStorage.frpcClientExectuablePath;
    }

    public boolean isFrpcAutoConfigureEnabled(){
        return configStorage.frpcAutoConfigure;
    }

    private class ConfigStorage {
        private List<MCSMDaemon> mcsmDaemons = new ArrayList<>();
        private String frpcClientCommonConfig = "[common]\nserver_addr=127.0.0.1\nserver_port=1145\ntcp_mux=true\nprotocol=tcp\nuser=user_name\ntoken=user_token";
        private String frpcClientPortConfig = "[{0}]\nprivilege_mode=true\ntype={1}\nlocal_ip=127.0.0.1\nlocal_port={2}\nremote_port={3}\nuse_encryption=true\nuse_compression=false";

        private String frpcClientExectuablePath = "./frpc";
        private boolean frpcAutoConfigure = false;
    }
}
