package net.zhuruoling.omms.mcsm.daemon.data.instance;

import java.util.List;

public class InstanceInfoData {
    long fileLock;
    int maxPlayers;
    boolean openFrpStatus;
    int currentPlayers;

    List<String> playersChart;
    String version;

    public long getFileLock() {
        return fileLock;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isOpenFrpStatus() {
        return openFrpStatus;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public List<String> getPlayersChart() {
        return playersChart;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "InstanceInfoData{" +
                "fileLock=" + fileLock +
                ", maxPlayers=" + maxPlayers +
                ", openFrpStatus=" + openFrpStatus +
                ", currentPlayers=" + currentPlayers +
                ", playersChart=" + playersChart +
                ", version='" + version + '\'' +
                '}';
    }
}
