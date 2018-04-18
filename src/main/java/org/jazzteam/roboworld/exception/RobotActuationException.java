package org.jazzteam.roboworld.exception;

public class RobotActuationException extends RuntimeException {
    public RobotActuationException() {
        super();
    }
    public RobotActuationException(String message) {
        super(message);
    }
    public RobotActuationException(String message, Throwable cause) {
        super(message, cause);
    }
    public RobotActuationException(Throwable cause) {
        super(cause);
    }
    protected RobotActuationException(String message, Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
