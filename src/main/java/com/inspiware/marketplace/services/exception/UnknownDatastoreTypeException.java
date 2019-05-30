package com.inspiware.marketplace.services.exception;

public class UnknownDatastoreTypeException extends RuntimeException {
    public UnknownDatastoreTypeException() {
    }

    public UnknownDatastoreTypeException(String message) {
        super(message);
    }

    public UnknownDatastoreTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownDatastoreTypeException(Throwable cause) {
        super(cause);
    }

    public UnknownDatastoreTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
