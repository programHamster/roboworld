package org.jazzteam.roboworld.model.exception.unsupported;

public class UnsupportedCommandException extends UnsupportedException {
    public UnsupportedCommandException() {
        super();
    }
    public UnsupportedCommandException(String message) {
        super(message);
    }
    public UnsupportedCommandException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnsupportedCommandException(Throwable cause) {
        super(cause);
    }
    protected UnsupportedCommandException(String message, Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected String getNameFunction(){
        return "command";
    }
}
