package org.jazzteam.roboworld.model.exception;

public class RobotDeadException extends RuntimeException {
    public RobotDeadException() {
        super();
    }
    public RobotDeadException(String message) {
        super(message);
    }
    public RobotDeadException(String message, Throwable cause) {
        super(message, cause);
    }
    public RobotDeadException(Throwable cause) {
        super(cause);
    }
    protected RobotDeadException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
