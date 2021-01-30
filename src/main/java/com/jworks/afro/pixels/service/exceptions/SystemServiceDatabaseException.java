package com.jworks.afro.pixels.service.exceptions;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */
public class SystemServiceDatabaseException extends SystemServiceException {
    public SystemServiceDatabaseException() {
    }

    public SystemServiceDatabaseException(String message, Throwable inner) {
        super(message, inner);
    }

    public SystemServiceDatabaseException(Throwable inner) {
        super((String)null, inner);
    }

    public SystemServiceDatabaseException(String message) {
        super(message);
    }

    public SystemServiceDatabaseException(String message, Object[] args) {
        super(message, args);
    }
}
