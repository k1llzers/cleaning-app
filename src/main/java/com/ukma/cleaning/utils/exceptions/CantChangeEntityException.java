package com.ukma.cleaning.utils.exceptions;

public class CantChangeEntityException extends RuntimeException {
    public CantChangeEntityException(String message) {
        super(message);
    }

    public CantChangeEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
