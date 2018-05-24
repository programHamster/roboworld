package by.roboworld.exception;

public class RobotNotFoundException extends RuntimeException {
    private String robotName;

    public RobotNotFoundException() {
        super();
    }
    public RobotNotFoundException(String robotName) {
        this.robotName = robotName;
    }
    public RobotNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public RobotNotFoundException(Throwable cause) {
        super(cause);
    }
    protected RobotNotFoundException(String message, Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getMessage(){
        String message;
        if(robotName != null){
            message = "robot " + (robotName == null ? "" : ("named \"" + robotName + "\" ")) + "not found";
        } else {
            message = super.getMessage();
        }
        return message;
    }
}
