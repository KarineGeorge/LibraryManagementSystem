package com.Maids.LibraryManagementSystem.Exceptions;

public class PatronIntegrityConstraintException extends RuntimeException{
    public PatronIntegrityConstraintException(String message) {
        super(message);
    }

    public PatronIntegrityConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}
