package org.jazzteam.roboworld.model.exception;

public class TaskNotFoundException extends RuntimeException {
    private String taskName;
    private String additionalMessage;

    public TaskNotFoundException() {
        super();
    }
    public TaskNotFoundException(String taskName) {
        this.taskName = taskName;
    }
    public TaskNotFoundException(String taskName, String additionalMessage) {
        this.taskName = taskName;
        this.additionalMessage = additionalMessage;
    }
    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public TaskNotFoundException(Throwable cause) {
        super(cause);
    }
    protected TaskNotFoundException(String message, Throwable cause,
                                    boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getMessage(){
        String message;
        if(taskName != null){
            message = "task named \"" + taskName + "\" is not found";
            if(additionalMessage != null){
                message += additionalMessage;
            }
        } else {
            message = super.getMessage();
        }
        return message;
    }
}
