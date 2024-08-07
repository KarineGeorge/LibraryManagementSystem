package com.Maids.LibraryManagementSystem.Exceptions;

public class PatronNotFoundException extends RuntimeException{
    public PatronNotFoundException(String message) {
        super(message);
    }

    public PatronNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
