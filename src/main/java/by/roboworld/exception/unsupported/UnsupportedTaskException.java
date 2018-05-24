package by.roboworld.exception.unsupported;

public class UnsupportedTaskException extends UnsupportedException {
    public UnsupportedTaskException() {
        super();
    }
    public UnsupportedTaskException(String name) {
        super(name);
    }
    public UnsupportedTaskException(Throwable cause) {
        super(cause);
    }
    protected UnsupportedTaskException(String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected String getNameFunction(){
        return "task";
    }

}
