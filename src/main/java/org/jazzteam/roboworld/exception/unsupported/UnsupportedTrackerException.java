package org.jazzteam.roboworld.exception.unsupported;

public class UnsupportedTrackerException extends UnsupportedException {
    private String message;

    public UnsupportedTrackerException() {
        super();
    }
    public UnsupportedTrackerException(String message) {
        this.message = message;
    }
    public UnsupportedTrackerException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnsupportedTrackerException(Throwable cause) {
        super(cause);
    }
    protected UnsupportedTrackerException(String message, Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected String getNameFunction(){
        return "tracker";
    }

    public String getMessage(){
        return message != null ? message : super.getMessage();
    }
}
