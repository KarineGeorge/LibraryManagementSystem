package com.Maids.LibraryManagementSystem.Exceptions;

public class BookIntegrityConstraintException extends RuntimeException {
    public BookIntegrityConstraintException(String message) {
        super(message);
    }

    public BookIntegrityConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}
