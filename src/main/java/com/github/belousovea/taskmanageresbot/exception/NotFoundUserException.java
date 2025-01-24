package com.github.belousovea.taskmanageresbot.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(long id) {
        super(String.format("User with id %d not found", id));
    }
}
