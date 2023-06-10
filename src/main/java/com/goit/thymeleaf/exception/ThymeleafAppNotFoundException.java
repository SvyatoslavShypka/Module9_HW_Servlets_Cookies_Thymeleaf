package com.goit.thymeleaf.exception;

public class ThymeleafAppNotFoundException extends RuntimeException {
    public ThymeleafAppNotFoundException() {
    }

    public ThymeleafAppNotFoundException(String message) {
        super(message);
    }

    public ThymeleafAppNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
