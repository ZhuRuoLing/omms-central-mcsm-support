package net.zhuruoling.omms.mcsm.daemon;

import net.zhuruoling.omms.central.controller.Controller;
import net.zhuruoling.omms.central.controller.Status;
import net.zhuruoling.omms.central.controller.console.ControllerConsole;
import net.zhuruoling.omms.central.controller.console.input.InputSource;
import net.zhuruoling.omms.central.controller.console.output.PrintTarget;
import net.zhuruoling.omms.central.controller.crashreport.CrashReportStorage;

import java.util.List;

public class MCSMDaemonController extends Controller {

    private final String name;
    private final String instanceName;
    private final MCSMDaemonInstance instance;

    public MCSMDaemonController(MCSMDaemonInstance daemon) {
        this.name = daemon.getDisplayName();
        instanceName = daemon.getInstanceData().getConfig().getNickname();
        this.instance = daemon;
    }

    @Override
    public boolean isStatusQueryable() {
        return true;
    }

    @Override
    public List<String> sendCommand(String s) {
        return instance.getDaemonConnector().executeCommand(this.instanceName, s);
    }

    @Override
    public ControllerConsole startControllerConsole(InputSource inputSource, PrintTarget<?, ControllerConsole> printTarget, String consoleId) {
        return new MCSMDaemonControllerConsole(this, consoleId, inputSource, printTarget);
    }

    @Override
    public Status queryControllerStatus() {
        return instance.getDaemonConnector().fetchInstanceInfoToStatus(this.instanceName);
    }

    @Override
    public CrashReportStorage convertCrashReport(String s) {
        return new CrashReportStorage(name, 0L,List.of());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "mcsmanager_instance";
    }

    public DaemonConnector getConnector(){
        return this.instance.getDaemonConnector();
    }

    public String getInstanceName() {
        return instanceName;
    }
}
