package by.roboworld.exception.unsupported;

public class UnsupportedRobotTypeException extends UnsupportedException {
    public UnsupportedRobotTypeException() {
        super();
    }
    public UnsupportedRobotTypeException(String name) {
        super(name);
    }
    public UnsupportedRobotTypeException(Throwable cause) {
        super(cause);
    }
    protected UnsupportedRobotTypeException(String message, Throwable cause,
                                            boolean enableSuppression,
                                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected String getNameFunction(){
        return "robot type";
    }

}
