package com.ukma.cleaning.utils;

public class NoSuchEntityException extends RuntimeException {
    public NoSuchEntityException() {
    }

    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
