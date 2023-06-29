package net.zhuruoling.omms.mcsm.process;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import net.zhuruoling.omms.central.system.runner.RunnerDaemon;
import net.zhuruoling.omms.central.util.Util;
import net.zhuruoling.omms.mcsm.config.Config;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.util.FileUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FrpcClient {
    private static final FrpcClient instance = new FrpcClient();
    private final Logger logger = LoggerFactory.getLogger("FrpcClient");
    private boolean running = false;
    private String configCommonPart = "";
    private List<FrpcClientInfo> clients = new ArrayList<>();
    private RunnerDaemon runnerDaemon;
    private File tempDir = new File("");

    public void launch() {
        logger.info("Starting frpc client.");
        StringBuilder stringBuilder = new StringBuilder(configCommonPart);
        clients.forEach(clientInfo -> stringBuilder.append("\n\n").append(clientInfo.formatConfigParts()));
        var tempId = Util.randomStringGen(16);
        tempDir = new File(Util.joinFilePaths(tempId));
        try {
            if (tempDir.exists()) FileUtils.deleteDirectory(tempDir);
            if (!tempDir.exists()) tempDir.mkdir();
            var frpcFilePath = tempDir.toPath().resolve("frpc").toFile();
            var frpcConfigFilePath = tempDir.toPath().resolve("frpc.ini").toFile();
            if (frpcConfigFilePath.exists()) frpcFilePath.delete();
            if (!frpcConfigFilePath.exists()) frpcFilePath.createNewFile();
            FileUtils.copyFile(new File(Config.INSTANCE.getFrpcClientExecutablePath()), frpcFilePath);
            try (var os = new BufferedOutputStream(new FileOutputStream(frpcConfigFilePath))) {
                os.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
            runnerDaemon = createRunnerDaemon(tempId, tempDir.getAbsolutePath());
            runnerDaemon.start();
        } catch (Exception e) {
            logger.error("Cannot launch frpc client.", e);
        }
    }

    private RunnerDaemon createRunnerDaemon(String tempId, String workingDir) {
        return new RunnerDaemon("frpc_client_%s".formatted(tempId), "frpc -c frpc.ini", workingDir, "Frpc Client", this::onProcessPrint, s -> s.substring(s.indexOf("[")), s -> true, this::onProcessExitCallback);
    }


    private Unit onProcessPrint(String s) {
        logger.debug("[frpc client] " + s);
        if (s.contains("[W]")) logger.warn("[Frpc Client] " + s);
        if (s.contains("[E]")) logger.error("[Frpc Client] " + s);
        return Unit.INSTANCE;
    }

    private Unit onProcessExitCallback(Process process) {
        return Unit.INSTANCE;
    }

    public void stop() {
        stop(false);
    }

    public void stop(boolean checkIsRunning) {
        if (!running && checkIsRunning) return;
        logger.info("Stopping client");
        if (runnerDaemon != null) {
            try {
                runnerDaemon.terminate();
            } catch (Exception e) {
                logger.warn(e.toString());
            }
        } else {
            logger.info("Frpc client thread is not running.");
        }
        if (tempDir.exists()) {
            try {
                FileUtils.deleteDirectory(tempDir);
            } catch (IOException e) {
                logger.warn("Cannot delete temp dir. {}", e.toString());
            }
        }
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setClients(List<FrpcClientInfo> clients) {
        this.clients = clients;
    }

    public static FrpcClient getInstance() {
        return instance;
    }


    public void setConfigCommonPart(String configCommonPart) {
        this.configCommonPart = configCommonPart;
    }
}
