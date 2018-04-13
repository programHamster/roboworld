package org.jazzteam.roboworld.model.exception;

public class RobotNotFoundException extends Exception {
    public RobotNotFoundException() {
        super();
    }
    public RobotNotFoundException(String message) {
        super(message);
    }
    public RobotNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public RobotNotFoundException(Throwable cause) {
        super(cause);
    }
    protected RobotNotFoundException(String message, Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
