package com.example.UserService.ExceptionHandler;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}