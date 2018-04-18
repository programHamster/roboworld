package org.jazzteam.roboworld.model.exception.unsupported;

public class UnsupportedTaskException extends UnsupportedException {
    public UnsupportedTaskException() {
        super();
    }
    public UnsupportedTaskException(String message) {
        super(message);
    }
    public UnsupportedTaskException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnsupportedTaskException(Throwable cause) {
        super(cause);
    }
    protected UnsupportedTaskException(String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected String getNameFunction(){
        return "task";
    }

}
