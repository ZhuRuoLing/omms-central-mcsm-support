package net.zhuruoling.omms.mcsm.daemon.exception;

public class InstanceNotFoundException extends RuntimeException{
    public InstanceNotFoundException(String message) {
        super(message);
    }

    public InstanceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
