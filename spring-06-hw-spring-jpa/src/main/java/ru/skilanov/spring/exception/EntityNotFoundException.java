package ru.skilanov.spring.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {

    }

    public EntityNotFoundException(String message) {
        super(message);
    }

}
