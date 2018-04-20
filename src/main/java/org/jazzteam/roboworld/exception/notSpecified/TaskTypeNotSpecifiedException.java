package org.jazzteam.roboworld.exception.notSpecified;

import org.jazzteam.roboworld.exception.Constants;

public class TaskTypeNotSpecifiedException extends RuntimeException {
    public TaskTypeNotSpecifiedException() {
        super();
    }
    public TaskTypeNotSpecifiedException(String message) {
        super(message);
    }
    public TaskTypeNotSpecifiedException(String message, Throwable cause) {
        super(message, cause);
    }
    public TaskTypeNotSpecifiedException(Throwable cause) {
        super(cause);
    }
    protected TaskTypeNotSpecifiedException(String message, Throwable cause,
                                            boolean enableSuppression,
                                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getMessage(){
        return Constants.TASK_TYPE_NOT_SPECIFIED;
    }
}
