package com.erommel.exception;

public class EntityNotValidException extends RuntimeException {
    public EntityNotValidException() {
        this("Entity not valid!");
    }

    public EntityNotValidException(String message) {
        super(message);
    }

    public EntityNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotValidException(Throwable cause) {
        super(cause);
    }

    public EntityNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}