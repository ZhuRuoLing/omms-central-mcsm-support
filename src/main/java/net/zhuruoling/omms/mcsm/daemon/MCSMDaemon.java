package net.zhuruoling.omms.mcsm.daemon;

public class MCSMDaemon {
    private String accessToken;
    private String address;
    private String name;

    public MCSMDaemon(String accessToken, String address, String name) {
        this.accessToken = accessToken;
        this.address = address;
        this.name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}


