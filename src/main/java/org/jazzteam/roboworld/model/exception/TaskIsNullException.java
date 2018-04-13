package org.jazzteam.roboworld.model.exception;

public class TaskIsNullException extends RuntimeException {
    public TaskIsNullException() {
        super();
    }
    public TaskIsNullException(String message) {
        super(message);
    }
    public TaskIsNullException(String message, Throwable cause) {
        super(message, cause);
    }
    public TaskIsNullException(Throwable cause) {
        super(cause);
    }
    protected TaskIsNullException(String message, Throwable cause,
                                  boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
