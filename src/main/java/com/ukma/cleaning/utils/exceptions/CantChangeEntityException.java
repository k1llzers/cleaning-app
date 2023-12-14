package com.ukma.cleaning.utils.exceptions;

public class CantChangeEntityException extends RuntimeException {
    public CantChangeEntityException(String message) {
        super(message);
    }
}
