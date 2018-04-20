package org.jazzteam.roboworld.exception.notSpecified;

import org.jazzteam.roboworld.exception.Constants;

public class RobotTypeNotSpecifiedException extends RuntimeException {
    public RobotTypeNotSpecifiedException() {
        super();
    }
    public RobotTypeNotSpecifiedException(String message) {
        super(message);
    }
    public RobotTypeNotSpecifiedException(String message, Throwable cause) {
        super(message, cause);
    }
    public RobotTypeNotSpecifiedException(Throwable cause) {
        super(cause);
    }
    protected RobotTypeNotSpecifiedException(String message, Throwable cause,
                                             boolean enableSuppression,
                                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getMessage(){
        return Constants.ROBOT_TYPE_NOT_SPECIFIED;
    }
}