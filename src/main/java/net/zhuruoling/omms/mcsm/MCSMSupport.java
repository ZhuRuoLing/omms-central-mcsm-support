package net.zhuruoling.omms.mcsm;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kotlin.Pair;
import net.zhuruoling.omms.central.command.CommandSourceStack;
import net.zhuruoling.omms.central.controller.ControllerManager;
import net.zhuruoling.omms.central.plugin.PluginMain;
import net.zhuruoling.omms.central.plugin.callback.CommandRegistrationCallback;
import net.zhuruoling.omms.central.plugin.callback.ControllerLoadCallback;
import net.zhuruoling.omms.mcsm.daemon.DaemonConnector;
import net.zhuruoling.omms.mcsm.daemon.MCSMDaemon;
import net.zhuruoling.omms.mcsm.daemon.MCSMDaemonController;
import net.zhuruoling.omms.mcsm.exception.PluginInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MCSMSupport extends PluginMain {

    private final Logger logger = LoggerFactory.getLogger("MCSMSupport");

    @Override
    public void onInitialize() {
        if (Config.INSTANCE.readConfig()) {
            List<MCSMDaemon> list = List.of(new MCSMDaemon("access token", "127.0.0.1:24444", "my_mcsm_daemon"));
            logger.info("Example Config for MCSM Support Plugin:\n %s".formatted(Util.prettyPrintingGson.toJson(list)));
            throw new PluginInitializationException("Config file not exist, default empty config created, this plugin will not load.");
        }

        ControllerLoadCallback.INSTANCE.register(controllerManager ->
                Config.INSTANCE.getMcsmDaemons().forEach(daemon ->
                        Config.INSTANCE.run(m -> {
                            try {
                                var connector = new DaemonConnector(daemon, null);
                                connector.connect();
                                connector.fetchInstances();
                                connector.getInstances().forEach(mcsmDaemonInstance -> controllerManager.addController(new MCSMDaemonController(mcsmDaemonInstance)));
                                m.put(daemon, connector);
                            } catch (Exception e) {
                                logger.error("An exception occurred while adding controller.", e);
                            }
                        })
                )
        );

        CommandRegistrationCallback.INSTANCE.register(commandManager -> {
            commandManager.getCommandDispatcher().register(LiteralArgumentBuilder.<CommandSourceStack>literal("mcsm")
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("refresh")
                            .executes(commandContext -> {
                                handleRefresh();
                                return 0;
                            })
                    )
            );
        });
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
            Config.INSTANCE.getMcsmDaemons().forEach(daemon ->
                    Config.INSTANCE.run(m -> {
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
                    })
            );
        }
    }
}
