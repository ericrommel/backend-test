package com.erommel.exception;

public class TransactionNotValidException extends RuntimeException {
    public TransactionNotValidException() {
        this("Transaction not valid!");
    }

    public TransactionNotValidException(String message) {
        super(message);
    }

    public TransactionNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionNotValidException(Throwable cause) {
        super(cause);
    }

    public TransactionNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}