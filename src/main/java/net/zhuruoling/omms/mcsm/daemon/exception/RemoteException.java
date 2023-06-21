package net.zhuruoling.omms.mcsm.daemon.exception;

public class RemoteException extends RuntimeException{
    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }
}
