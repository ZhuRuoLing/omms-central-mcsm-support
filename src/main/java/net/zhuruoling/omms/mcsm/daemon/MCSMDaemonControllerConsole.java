package net.zhuruoling.omms.mcsm.daemon;

import net.zhuruoling.omms.central.controller.Controller;
import net.zhuruoling.omms.central.controller.console.ControllerConsole;
import net.zhuruoling.omms.central.controller.console.input.InputSource;
import net.zhuruoling.omms.central.controller.console.output.PrintTarget;

public class MCSMDaemonControllerConsole implements ControllerConsole {

    private final MCSMDaemonController controller;
    private final String consoleId;

    private final InputSource inputSource;
    private final PrintTarget<?, ControllerConsole> printTarget;

    public MCSMDaemonControllerConsole(MCSMDaemonController controller, String consoleId, InputSource inputSource, PrintTarget<?, ControllerConsole> printTarget) {
        this.controller = controller;
        this.consoleId = consoleId;
        this.inputSource = inputSource;
        this.printTarget = printTarget;
    }

    @Override
    public Controller getController() {
        return controller;
    }

    @Override
    public String getConsoleId() {
        return consoleId;
    }

    @Override
    public void start() {

    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public InputSource getInputSource() {
        return null;
    }

    @Override
    public void close() {

    }
}
