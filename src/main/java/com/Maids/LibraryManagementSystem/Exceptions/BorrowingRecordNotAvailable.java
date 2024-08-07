package com.Maids.LibraryManagementSystem.Exceptions;

public class BorrowingRecordNotAvailable extends RuntimeException{
    public BorrowingRecordNotAvailable(String message) {
        super(message);
    }

    public BorrowingRecordNotAvailable(String message, Throwable cause) {
        super(message, cause);
    }
}
