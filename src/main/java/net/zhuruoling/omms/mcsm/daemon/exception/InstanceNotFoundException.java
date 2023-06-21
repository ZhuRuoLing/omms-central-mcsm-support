package net.zhuruoling.omms.mcsm.daemon.exception;

public class InstanceNotFoundException extends RuntimeException{
    public InstanceNotFoundException(String instance) {
        super("Instance %s not found".formatted(instance));
    }

    public InstanceNotFoundException(String instance, Throwable cause) {
        super("Instance %s not found".formatted(instance), cause);
    }
}
