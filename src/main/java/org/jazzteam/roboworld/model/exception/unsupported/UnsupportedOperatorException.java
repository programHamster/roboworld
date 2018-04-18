package org.jazzteam.roboworld.model.exception.unsupported;

public class UnsupportedOperatorException extends UnsupportedException {
    private String message;

    public UnsupportedOperatorException() {
        super();
    }
    public UnsupportedOperatorException(String message) {
        this.message = message;
    }
    public UnsupportedOperatorException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnsupportedOperatorException(Throwable cause) {
        super(cause);
    }
    protected UnsupportedOperatorException(String message, Throwable cause,
                                           boolean enableSuppression,
                                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected String getNameFunction(){
        return "operator";
    }

    public String getMessage(){
        return message != null ? message : super.getMessage();
    }
}
