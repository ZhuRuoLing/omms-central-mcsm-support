package net.zhuruoling.omms.mcsm.daemon.data.instance;

public class InstanceTerminalOptionData {
    boolean pty;
    boolean haveColor;
    int ptyWindowRow;
    int ptyWindowCol;

    public boolean isPty() {
        return pty;
    }

    public boolean isHaveColor() {
        return haveColor;
    }

    public int getPtyWindowRow() {
        return ptyWindowRow;
    }

    public int getPtyWindowCol() {
        return ptyWindowCol;
    }
}
