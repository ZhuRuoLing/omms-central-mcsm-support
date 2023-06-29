package net.zhuruoling.omms.mcsm.daemon;

import kotlin.Unit;
import java.lang.invoke.*;
import net.zhuruoling.omms.central.controller.Controller;
import net.zhuruoling.omms.central.controller.console.ControllerConsole;
import net.zhuruoling.omms.central.controller.console.input.InputSource;
import net.zhuruoling.omms.central.controller.console.output.PrintTarget;

import java.util.Arrays;

public class MCSMDaemonControllerConsole extends Thread implements ControllerConsole {

    private final MCSMDaemonController controller;
    private final String consoleId;
    private String password;
    private final String instanceName;
    private final InputSource inputSource;
    private final PrintTarget<?, ControllerConsole> printTarget;

    public MCSMDaemonControllerConsole(MCSMDaemonController controller, String consoleId, InputSource inputSource, PrintTarget<?, ControllerConsole> printTarget) {
        super("MCSMDaemonConsoleThread");
        this.controller = controller;
        this.consoleId = consoleId;
        this.inputSource = inputSource;
        this.printTarget = printTarget;
        instanceName = controller.getInstanceName();
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
    public void run() {
        printTarget.println("-------- Console Log History -------", this);
        Arrays.stream(controller.getConnector().fetchInstanceLog(instanceName).split("\n")).forEach(s -> printTarget.println(s, this));
        password = controller.getConnector().startStreamRedirect(instanceName, (line, uuid) -> {
            printTarget.println(line, this);
            return Unit.INSTANCE;
        });
        printTarget.println("-------- Console Log History -------", this);
        while (true){
            try {
                String line = inputSource.getLine();
                if (line == null || line.isEmpty()) continue;
                if (line.equals(":q"))break;
                controller.getConnector().streamInput(instanceName, password, line);
                sleep(50);
            }catch (Exception e){
                if (e instanceof InterruptedException)break;
                e.printStackTrace();
            }
        }
    }

    @Override
    public InputSource getInputSource() {
        return inputSource;
    }

    @Override
    public void close() {
        controller.getConnector().abortStreamRedirect(instanceName, password);
        this.interrupt();
    }
}
