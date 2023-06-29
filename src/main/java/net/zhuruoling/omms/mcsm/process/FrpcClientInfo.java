package net.zhuruoling.omms.mcsm.process;

import java.text.MessageFormat;

public class FrpcClientInfo {
    private String clientConfigTemplate = "";
    private String portType = "";
    private String clientId = "";
    private String localPort = "";
    private String remotePort = "";

    public FrpcClientInfo(String clientConfigTemplate, String portType, String clientId, String localPort, String remotePort) {
        this.clientConfigTemplate = clientConfigTemplate;
        this.portType = portType;
        this.clientId = clientId;
        this.localPort = localPort;
        this.remotePort = remotePort;
    }

    public String formatConfigParts(){
        return MessageFormat.format(clientConfigTemplate, clientId, portType, localPort, remotePort);
    }

}
