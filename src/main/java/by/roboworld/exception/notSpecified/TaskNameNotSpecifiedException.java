package by.roboworld.exception.notSpecified;

import by.roboworld.exception.Constants;

public class TaskNameNotSpecifiedException extends RuntimeException {
    public TaskNameNotSpecifiedException() {
        super();
    }
    public TaskNameNotSpecifiedException(String message) {
        super(message);
    }
    public TaskNameNotSpecifiedException(String message, Throwable cause) {
        super(message, cause);
    }
    public TaskNameNotSpecifiedException(Throwable cause) {
        super(cause);
    }
    protected TaskNameNotSpecifiedException(String message, Throwable cause,
                                            boolean enableSuppression,
                                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getMessage(){
        return Constants.TASK_NAME_NOT_SPECIFIED;
    }
}
