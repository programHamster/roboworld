package org.jazzteam.roboworld.model.exception;

import org.jazzteam.roboworld.model.bean.robot.Robot;

public class RobotDeadException extends RuntimeException {
    private Robot robot;

    public RobotDeadException() {
        super();
    }
    public RobotDeadException(Robot robot){
        this.robot = robot;
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
    public RobotDeadException(Robot robot, Throwable cause) {
        this(cause);
        this.robot = robot;
    }
    protected RobotDeadException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getMessage(){
        String message;
        if(robot != null){
            message = "the robot named \"" + robot.getName() + "\" is dead";
        } else {
            message = super.getMessage();
        }
        return message;
    }
}
