package com.jworks.afro.pixels.service.exceptions;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */
public class SystemServiceException extends Exception {

    protected Object[] args;

    public SystemServiceException() {
    }

    public SystemServiceException(String message, Throwable inner) {
        super(message, inner);
    }

    public SystemServiceException(String message) {
        super(message);
    }

    public SystemServiceException(String message, Object[] args) {
        super(message);
        this.args = args;
    }

    public Object[] getArgs() {
        return this.args;
    }
}
