package net.zhuruoling.omms.mcsm.daemon.data.instance;

import java.util.List;

public class InstanceConfigData {//InstanceConfig
    InstanceEventTaskData eventTask;
    /**
     * 1: \n  2: \r\n
     */
    int crlf;
    InstanceTerminalOptionData terminalOption;
    List<InstanceActionCommandList> actionCommandList;

    /**
     * wts dis?
     */
    String fileCode;

    String startCommand;

    String lastDateTime;

    String type;

    InstancePingConfig pingConfig;
    InstanceDockerConfigData docker;

    InstanceExtraServiceConfig extraServiceConfig;

    String createDateTime;
    String cwd;
    String oe;
    String nickname;
    String stopCommand;
    String updateCommand;
    List<String> tag;
    String endTime;
    String processType;
    String ie;

    public InstanceEventTaskData getEventTask() {
        return eventTask;
    }

    public int getCrlf() {
        return crlf;
    }

    public InstanceTerminalOptionData getTerminalOption() {
        return terminalOption;
    }

    public List<InstanceActionCommandList> getActionCommandList() {
        return actionCommandList;
    }

    public String getFileCode() {
        return fileCode;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public String getLastDateTime() {
        return lastDateTime;
    }

    public String getType() {
        return type;
    }

    public InstancePingConfig getPingConfig() {
        return pingConfig;
    }

    public InstanceDockerConfigData getDocker() {
        return docker;
    }

    public InstanceExtraServiceConfig getExtraServiceConfig() {
        return extraServiceConfig;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public String getCwd() {
        return cwd;
    }

    public String getOe() {
        return oe;
    }

    public String getNickname() {
        return nickname;
    }

    public String getStopCommand() {
        return stopCommand;
    }

    public String getUpdateCommand() {
        return updateCommand;
    }

    public List<String> getTag() {
        return tag;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getProcessType() {
        return processType;
    }

    public String getIe() {
        return ie;
    }

    @Override
    public String toString() {
        return "InstanceConfigData{" +
                "eventTask=" + eventTask +
                ", crlf=" + crlf +
                ", terminalOption=" + terminalOption +
                ", actionCommandList=" + actionCommandList +
                ", fileCode='" + fileCode + '\'' +
                ", startCommand='" + startCommand + '\'' +
                ", lastDateTime='" + lastDateTime + '\'' +
                ", type='" + type + '\'' +
                ", pingConfig=" + pingConfig +
                ", docker=" + docker +
                ", extraServiceConfig=" + extraServiceConfig +
                ", createDateTime='" + createDateTime + '\'' +
                ", cwd='" + cwd + '\'' +
                ", oe='" + oe + '\'' +
                ", nickname='" + nickname + '\'' +
                ", stopCommand='" + stopCommand + '\'' +
                ", updateCommand='" + updateCommand + '\'' +
                ", tag=" + tag +
                ", endTime='" + endTime + '\'' +
                ", processType='" + processType + '\'' +
                ", ie='" + ie + '\'' +
                '}';
    }
}
