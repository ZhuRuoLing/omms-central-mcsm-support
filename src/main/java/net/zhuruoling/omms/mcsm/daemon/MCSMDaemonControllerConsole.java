package net.zhuruoling.omms.mcsm.daemon;

import net.zhuruoling.omms.central.controller.Controller;
import net.zhuruoling.omms.central.controller.console.ControllerConsole;
import net.zhuruoling.omms.central.controller.console.input.InputSource;

public class MCSMDaemonControllerConsole implements ControllerConsole {
    @Override
    public Controller getController() {
        return null;
    }

    @Override
    public String getConsoleId() {
        return null;
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
