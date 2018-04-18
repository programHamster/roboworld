package org.jazzteam.roboworld.model.exception;

import org.jazzteam.roboworld.model.bean.robot.Robot;

public class RobotAlreadyExistException extends RuntimeException {
    private Robot robot;

    public RobotAlreadyExistException() {
        super();
    }
    public RobotAlreadyExistException(String message) {
        super(message);
    }
    public RobotAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
    public RobotAlreadyExistException(Throwable cause) {
        super(cause);
    }
    protected RobotAlreadyExistException(String message, Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RobotAlreadyExistException(Robot robot) {
        this.robot = robot;
    }
    public RobotAlreadyExistException(Robot robot, Throwable cause) {
        this(cause);
        this.robot = robot;
    }

    public String getMessage(){
        String message;
        if(robot != null){
            message = "The robot named \"" + robot.getName() + "\" already exists";
        } else {
            message = super.getMessage();
        }
        return message;
    }
}
