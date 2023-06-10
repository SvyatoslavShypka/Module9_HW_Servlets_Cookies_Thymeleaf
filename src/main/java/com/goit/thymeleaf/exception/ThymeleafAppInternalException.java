package com.goit.thymeleaf.exception;

public class ThymeleafAppInternalException extends RuntimeException {
    public ThymeleafAppInternalException() {
    }

    public ThymeleafAppInternalException(String message) {
        super(message);
    }

    public ThymeleafAppInternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
