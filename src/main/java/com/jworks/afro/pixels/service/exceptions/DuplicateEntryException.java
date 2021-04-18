package com.jworks.afro.pixels.service.exceptions;

/**
 * @author Johnpaul Chukwu.
 * @since 31/12/2020
 */
public class DuplicateEntryException extends SystemServiceDatabaseException {
    public DuplicateEntryException() {
    }

    public DuplicateEntryException(String message, Throwable inner) {
        super(message, inner);
    }

    public DuplicateEntryException(Throwable inner) {
        super((String)null, inner);
    }

    public DuplicateEntryException(String message) {
        super(message);
    }

    public DuplicateEntryException(String message, Object[] args) {
        super(message, args);
    }
}

