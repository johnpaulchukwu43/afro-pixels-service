package com.jworks.afro.pixels.service.exceptions;

/**
 * @author Johnpaul Chukwu.
 * @since 24/12/2020
 */
public class BadRequestException extends SystemServiceException {
    public BadRequestException() {
    }

    public BadRequestException(String message, Throwable inner) {
        super(message, inner);
    }

    public BadRequestException(Throwable inner) {
        super((String)null, inner);
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Object[] args) {
        super(message, args);
    }
}
