package com.example.trippick.exception;

public class DuplicateUser extends RuntimeException {
    public DuplicateUser(String message) {
        super(message);
    }
}

