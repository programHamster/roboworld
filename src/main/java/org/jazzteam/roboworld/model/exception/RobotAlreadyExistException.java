package org.jazzteam.roboworld.model.exception;

public class RobotAlreadyExistException extends Exception {
    private String name;

    public RobotAlreadyExistException(String name) {
        this.name = name;
    }

    public String getMessage(){
        return "The robot named \"" + name + "\" already exists.";
    }
}
