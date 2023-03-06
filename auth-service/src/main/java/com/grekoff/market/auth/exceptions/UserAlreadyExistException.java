package com.grekoff.market.auth.exceptions;

public final class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
