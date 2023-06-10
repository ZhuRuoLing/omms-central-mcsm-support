package net.zhuruoling.omms.mcsm;

import net.zhuruoling.omms.central.plugin.PluginMain;
import net.zhuruoling.omms.central.plugin.callback.ControllerLoadCallback;
import net.zhuruoling.omms.mcsm.daemon.MCSMDaemonController;

public class MCSMSupport extends PluginMain {
    @Override
    public void onInitialize() {
        Config.INSTANCE.readConfig();
        ControllerLoadCallback.INSTANCE.register(controllerManager ->
                Config.INSTANCE.getMcsmDaemons().forEach(daemon ->
                        controllerManager.addController(new MCSMDaemonController(daemon))
                )
        );
    }
}
