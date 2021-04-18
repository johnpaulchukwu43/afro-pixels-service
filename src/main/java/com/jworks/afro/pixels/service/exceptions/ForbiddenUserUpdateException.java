package com.jworks.afro.pixels.service.exceptions;

/**
 * @author Johnpaul Chukwu.
 * @since 24/12/2020
 */
public class ForbiddenUserUpdateException extends SystemServiceException {
    public ForbiddenUserUpdateException() {
    }

    public ForbiddenUserUpdateException(String username1, String username2) {
        super(String.format("Forbidden. User: %s trying to update account for another user: %s", username1,username2));
    }

    public ForbiddenUserUpdateException(String message, Throwable inner) {
        super(message, inner);
    }

    public ForbiddenUserUpdateException(Throwable inner) {
        super((String)null, inner);
    }

    public ForbiddenUserUpdateException(String message) {
        super(message);
    }

    public ForbiddenUserUpdateException(String message, Object[] args) {
        super(message, args);
    }
}
