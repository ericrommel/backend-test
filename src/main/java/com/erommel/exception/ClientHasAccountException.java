package com.erommel.exception;

public class ClientHasAccountException extends RuntimeException {
    public ClientHasAccountException() {
        this("Transaction not valid!");
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