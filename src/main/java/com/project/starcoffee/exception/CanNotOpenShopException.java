package com.project.starcoffee.exception;

public class CanNotOpenShopException extends RuntimeException {
    public CanNotOpenShopException(String message) {
        super(message);
    }

    public CanNotOpenShopException(String message, Throwable cause) {
        super(message, cause);
    }
}
