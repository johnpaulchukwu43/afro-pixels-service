package com.jworks.afro.pixels.service.exceptions;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */
public class UnProcessableOperationException extends RestApiException {

    public UnProcessableOperationException(String message) {
        super(message);
    }

    public UnProcessableOperationException(Throwable ex) {
        super(ex);
    }

    public UnProcessableOperationException(Throwable cause, int code) {
        super(cause, code);
    }

    public UnProcessableOperationException(String message, int code) {
        super(message, code);
    }

    public UnProcessableOperationException(String message, Object[] args) {
        super(message, args);
    }
}
