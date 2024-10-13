package ru.springproject.utils;

public class CustomerNotFoundException extends RuntimeException {

    private final String message;

    public CustomerNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
