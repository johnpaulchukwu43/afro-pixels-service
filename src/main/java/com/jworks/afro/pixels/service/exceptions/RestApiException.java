package com.jworks.afro.pixels.service.exceptions;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */
public class RestApiException extends Exception {

    private static final String UNKNOWN_ERROR_MSG = "An unknown error has occured";
    protected Object[] args;
    private int code;

    public RestApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public RestApiException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public RestApiException(String message) {
        super(message);
    }

    public RestApiException(Throwable cause) {
        this((Throwable)cause, 0);
    }

    public RestApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestApiException(String message, Object[] args) {
        super(message);
        this.args = args;
    }

    public String getMessage() {
        Throwable cause = this.getCause();
        if (cause instanceof SystemServiceException) {
            return cause.getMessage();
        } else {
            return super.getMessage() != null && !super.getMessage().isEmpty() ? super.getMessage() : null;
        }
    }

    public Object[] getArgs() {
        return this.args;
    }

    public static RestApiException getUnknownException() {
        return new RestApiException(UNKNOWN_ERROR_MSG);
    }

    public static RestApiException getUnknownException(Throwable t) {
        t.printStackTrace();
        return new RestApiException(UNKNOWN_ERROR_MSG);
    }

    public int getCode() {
        return this.code;
    }
}
