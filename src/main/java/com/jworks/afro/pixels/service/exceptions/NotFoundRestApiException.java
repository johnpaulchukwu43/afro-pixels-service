package com.jworks.afro.pixels.service.exceptions;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */
public class NotFoundRestApiException extends RestApiException {

    public NotFoundRestApiException(String message) {
        super(message);
    }

    public NotFoundRestApiException(Throwable ex) {
        super(ex);
    }

    public NotFoundRestApiException(Throwable cause, int code) {
        super(cause, code);
    }

    public NotFoundRestApiException(String message, int code) {
        super(message, code);
    }

    public NotFoundRestApiException(String message, Object[] args) {
        super(message, args);
    }
}
