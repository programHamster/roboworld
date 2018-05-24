package by.roboworld.exception.unsupported;

public abstract class UnsupportedException extends Exception {
    private String name;

    public UnsupportedException() {
        super();
    }
    public UnsupportedException(String name) {
        this.name = name;
    }
    public UnsupportedException(Throwable cause) {
        super(cause);
    }
    protected UnsupportedException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected abstract String getNameFunction();

    public String getMessage(){
        return "Unsupported " + getNameFunction() + (name != null ? " \"" + name + "\"" : "");
    }
}
