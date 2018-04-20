package org.jazzteam.roboworld.exception;

public class TaskNotFoundException extends RuntimeException {
    private String taskName;

    public TaskNotFoundException() {
        super();
    }
    public TaskNotFoundException(String taskName) {
        this.taskName = taskName;
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
        } else {
            message = super.getMessage();
        }
        return message;
    }
}
