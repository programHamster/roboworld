package org.jazzteam.roboworld.exception.unsupported;

public class UnsupportedRobotTypeException extends UnsupportedException {
    public UnsupportedRobotTypeException() {
        super();
    }
    public UnsupportedRobotTypeException(String message) {
        super(message);
    }
    public UnsupportedRobotTypeException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnsupportedRobotTypeException(Throwable cause) {
        super(cause);
    }
    protected UnsupportedRobotTypeException(String message, Throwable cause,
                                            boolean enableSuppression,
                                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected String getNameFunction(){
        return "robot type";
    }

}
