package org.jazzteam.roboworld.exception;

import org.jazzteam.roboworld.model.bean.task.Task;

public class TaskNotFeasibleException extends RuntimeException {
    private String robotName;
    private Task task;

    public TaskNotFeasibleException() {
        super();
    }
    public TaskNotFeasibleException(String message) {
        super(message);
    }
    public TaskNotFeasibleException(String message, Throwable cause) {
        super(message, cause);
    }
    public TaskNotFeasibleException(Throwable cause) {
        super(cause);
    }
    protected TaskNotFeasibleException(String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TaskNotFeasibleException(String robotName, Task task) {
        this.robotName = robotName;
        this.task = task;
    }
    public TaskNotFeasibleException(String robotName, Task task, Throwable cause) {
        this(cause);
        this.robotName = robotName;
        this.task = task;
    }

    public String getMessage(){
        String message;
        if(robotName != null && task != null){
            message = "the robot \"" + robotName + "\" cannot perform the task \"" + task.getName() + "\"";
        } else {
            message = super.getMessage();
        }
        return message;
    }

}
