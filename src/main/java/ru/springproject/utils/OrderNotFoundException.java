package ru.springproject.utils;

public class OrderNotFoundException extends RuntimeException {
    private final String message;

    public OrderNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
