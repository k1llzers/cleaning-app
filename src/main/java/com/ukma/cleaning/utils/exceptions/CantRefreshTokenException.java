package com.ukma.cleaning.utils.exceptions;

public class CantRefreshTokenException extends RuntimeException {
    public CantRefreshTokenException(String message) {
        super(message);
    }
}
