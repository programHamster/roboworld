package org.jazzteam.roboworld.exception.unsupported;

public abstract class UnsupportedException extends Exception {

    public UnsupportedException() {
        super();
    }
    public UnsupportedException(String message) {
        super(message);
    }
    public UnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnsupportedException(Throwable cause) {
        super(cause);
    }
    protected UnsupportedException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected abstract String getNameFunction();

    public String getMessage(){
        return "Unsupported " + getNameFunction();
    }
}
