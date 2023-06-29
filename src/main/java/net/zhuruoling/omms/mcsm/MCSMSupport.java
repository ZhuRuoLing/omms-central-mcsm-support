package net.zhuruoling.omms.mcsm;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kotlin.Pair;
import net.zhuruoling.omms.central.command.CommandSourceStack;
import net.zhuruoling.omms.central.controller.ControllerManager;
import net.zhuruoling.omms.central.plugin.PluginMain;
import net.zhuruoling.omms.central.plugin.callback.CommandRegistrationCallback;
import net.zhuruoling.omms.central.plugin.callback.ControllerLoadCallback;
import net.zhuruoling.omms.mcsm.config.Config;
import net.zhuruoling.omms.mcsm.daemon.DaemonConnector;
import net.zhuruoling.omms.mcsm.daemon.MCSMDaemon;
import net.zhuruoling.omms.mcsm.daemon.MCSMDaemonController;
import net.zhuruoling.omms.mcsm.daemon.data.instance.InstanceData;
import net.zhuruoling.omms.mcsm.exception.PluginInitializationException;
import net.zhuruoling.omms.mcsm.process.FrpcClient;
import net.zhuruoling.omms.mcsm.process.FrpcClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;

public class MCSMSupport extends PluginMain {
    private final Logger logger = LoggerFactory.getLogger("MCSMSupport");

    @Override
    public void onInitialize() {
        if (Config.INSTANCE.readConfig()) {
            List<MCSMDaemon> list = List.of(new MCSMDaemon("access token", "127.0.0.1:24444", "my_mcsm_daemon"));
            logger.info("Example Config for MCSM Support Plugin:\n %s".formatted(Util.prettyPrintingGson.toJson(list)));
            throw new PluginInitializationException("Config file not exist, default empty config created, this plugin will not load.");
        }

        ControllerLoadCallback.INSTANCE.register(controllerManager -> Config.INSTANCE.getMcsmDaemons().forEach(daemon -> Config.INSTANCE.run(m -> {
            try {
                var connector = new DaemonConnector(daemon, null);
                connector.connect();
                connector.fetchInstances();
                connector.getInstances().forEach(mcsmDaemonInstance -> controllerManager.addController(new MCSMDaemonController(mcsmDaemonInstance)));
                m.put(daemon, connector);
            } catch (Exception e) {
                logger.error("An exception occurred while adding controller.", e);
            }
        })));

        CommandRegistrationCallback.INSTANCE.register(commandManager -> {
            commandManager.getCommandDispatcher().register(LiteralArgumentBuilder.<CommandSourceStack>literal("mcsm").then(LiteralArgumentBuilder.<CommandSourceStack>literal("refresh").executes(commandContext -> {
                handleRefresh();
                return 0;
            })).then(LiteralArgumentBuilder.<CommandSourceStack>literal("frpc").then(LiteralArgumentBuilder.<CommandSourceStack>literal("start").executes(commandContext -> {
                startFrpcServices();
                return 0;
            })).then(LiteralArgumentBuilder.<CommandSourceStack>literal("stop").executes(commandContext -> {
                stopFrpcServices();
                return 0;
            }))));
        });
    }

    private void startFrpcServices() {
        if (!Config.INSTANCE.isFrpcAutoConfigureEnabled()) return;
        try {
            List<InstanceData> list = new ArrayList<>();
            Config.INSTANCE.daemonConnectorMap.forEach((mcsmDaemon, daemonConnector) -> {
                if (mcsmDaemon.getAddress().contains("localhost") || mcsmDaemon.getAddress().contains("127.0.0.")) {
                    list.addAll(daemonConnector.fetchInstances().getData().stream().filter(it -> it.getConfig().getType().equals("docker")).toList());
                }
            });
            if (list.isEmpty()) {
                logger.info("There is no local instances, port forward using frpc will not work.");
                return;
            }
            List<FrpcClientInfo> clientInfos = new ArrayList<>();
            list.forEach(instanceData -> {
                String clientConfigTemplate = Config.INSTANCE.getFrpcClientPortConfig();
                AtomicReference<String> portType = new AtomicReference<>();
                AtomicReference<String> clientId = new AtomicReference<>();
                AtomicReference<String> localPort = new AtomicReference<>();
                AtomicReference<String> remotePort = new AtomicReference<>();
                instanceData.getConfig().getDocker().getPorts().forEach(s -> {
                    Matcher matcher = Util.dockerPortParsePattern.matcher(s);
                    if (matcher.matches()) {
                        portType.set(matcher.group(3));
                        clientId.set(instanceData.getConfig().getNickname().replace(" ", "_") + net.zhuruoling.omms.central.util.Util.randomStringGen(4));
                        remotePort.set(matcher.group(1));
                        localPort.set(remotePort.get());
                        clientInfos.add(new FrpcClientInfo(clientConfigTemplate, portType.get(), clientId.get(), localPort.get(), remotePort.get()));
                    }
                });
            });
            FrpcClient.getInstance().stop(true);
            FrpcClient.getInstance().setClients(clientInfos);
            FrpcClient.getInstance().setConfigCommonPart(Config.INSTANCE.getFrpcClientCommonConfig());
            FrpcClient.getInstance().launch();
        } catch (Exception e) {
            logger.error("Cannot start frpc service", e);
        }
    }

    private void stopFrpcServices() {
        if (!Config.INSTANCE.isFrpcAutoConfigureEnabled()) return;
        FrpcClient.getInstance().stop();
    }


    private void handleRefresh() {
        synchronized (Config.INSTANCE.daemonConnectorMap) {
            Config.INSTANCE.daemonConnectorMap.forEach((mcsmDaemon, daemonConnector) -> {
                daemonConnector.getInstances().forEach(mcsmDaemonInstance -> {
                    var removed = new ArrayList<Pair<String, MCSMDaemonController>>();
                    synchronized (ControllerManager.INSTANCE.getControllers()) {
                        ControllerManager.INSTANCE.getControllers().forEach((s, controller1) -> {
                            if (Objects.equals(s, mcsmDaemonInstance.getDisplayName()) && controller1 instanceof MCSMDaemonController) {
                                removed.add(new Pair<>(s, (MCSMDaemonController) controller1));
                            }
                        });
                        removed.forEach(pair -> ControllerManager.INSTANCE.getControllers().remove(pair.getFirst(), pair.getSecond()));
                    }
                });
                daemonConnector.close();
            });
            Config.INSTANCE.daemonConnectorMap.clear();
            Config.INSTANCE.readConfig();
            Config.INSTANCE.getMcsmDaemons().forEach(daemon -> Config.INSTANCE.run(m -> {
                try {
                    var connector = new DaemonConnector(daemon, null);
                    connector.connect();
                    connector.fetchInstances();
                    connector.getInstances().forEach(mcsmDaemonInstance -> {
                        ControllerManager.INSTANCE.addController(new MCSMDaemonController(mcsmDaemonInstance));
                    });
                    m.put(daemon, connector);
                } catch (Exception e) {
                    logger.error("An exception occurred while adding controller.", e);
                }
            }));
        }
    }
}
