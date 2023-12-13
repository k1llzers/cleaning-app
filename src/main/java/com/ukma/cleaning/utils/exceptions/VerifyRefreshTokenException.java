package com.ukma.cleaning.utils.exceptions;

public class VerifyRefreshTokenException extends RuntimeException {
    public VerifyRefreshTokenException(String message) {
        super(message);
    }

    public VerifyRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
