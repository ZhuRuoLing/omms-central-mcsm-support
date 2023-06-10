package net.zhuruoling.omms.mcsm.daemon;

import net.zhuruoling.omms.central.controller.Controller;
import net.zhuruoling.omms.central.controller.Status;
import net.zhuruoling.omms.central.controller.console.ControllerConsole;
import net.zhuruoling.omms.central.controller.console.input.InputSource;
import net.zhuruoling.omms.central.controller.console.output.PrintTarget;
import net.zhuruoling.omms.central.controller.crashreport.CrashReportStorage;

import java.util.List;

public class MCSMDaemonController extends Controller {
    public MCSMDaemonController(MCSMDaemon daemon){

    }

    @Override
    public boolean isStatusQueryable() {
        return false;
    }

    @Override
    public List<String> sendCommand(String s) {
        return null;
    }

    @Override
    public ControllerConsole startControllerConsole(InputSource inputSource, PrintTarget<?, ControllerConsole> printTarget, String s) {
        return null;
    }

    @Override
    public Status queryControllerStatus() {
        return null;
    }

    @Override
    public CrashReportStorage convertCrashReport(String s) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }
}
