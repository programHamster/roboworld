package by.roboworld.exception.notSpecified;

import by.roboworld.exception.Constants;

public class RobotNameNotSpecifiedException extends RuntimeException {
    public RobotNameNotSpecifiedException() {
        super();
    }
    public RobotNameNotSpecifiedException(String message) {
        super(message);
    }
    public RobotNameNotSpecifiedException(String message, Throwable cause) {
        super(message, cause);
    }
    public RobotNameNotSpecifiedException(Throwable cause) {
        super(cause);
    }
    protected RobotNameNotSpecifiedException(String message, Throwable cause,
                                             boolean enableSuppression,
                                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getMessage(){
        return Constants.ROBOT_NAME_NOT_SPECIFIED;
    }
}
