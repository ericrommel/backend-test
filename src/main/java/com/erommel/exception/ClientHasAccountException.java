package com.erommel.exception;

public class ClientHasAccountException extends RuntimeException {
    public ClientHasAccountException() {
        this("This client has one or more accounts and cannot be deleted");
    }

    public ClientHasAccountException(String message) {
        super(message);
    }

    public ClientHasAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientHasAccountException(Throwable cause) {
        super(cause);
    }

    public ClientHasAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}