package com.Maids.LibraryManagementSystem.Services;

import com.Maids.LibraryManagementSystem.Exceptions.BookNotAvailableException;
import com.Maids.LibraryManagementSystem.Exceptions.BookNotFoundException;
import com.Maids.LibraryManagementSystem.Exceptions.BorrowingRecordNotAvailable;
import com.Maids.LibraryManagementSystem.Exceptions.PatronNotFoundException;
import com.Maids.LibraryManagementSystem.Models.Book;
import com.Maids.LibraryManagementSystem.Models.BorrowingRecord;
import com.Maids.LibraryManagementSystem.Models.Patron;
import com.Maids.LibraryManagementSystem.Repositories.BookRepository;
import com.Maids.LibraryManagementSystem.Repositories.BorrowingRecordRepository;
import com.Maids.LibraryManagementSystem.Repositories.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final PatronRepository patronRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BorrowingRecordService(BorrowingRecordRepository borrowingRecordRepository, PatronRepository patronRepository, BookRepository bookRepository) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.patronRepository = patronRepository;
        this.bookRepository = bookRepository;
    }

    public BorrowingRecord addBorrowingRecord(Long bookId, Long patronId){
        Book book = bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException("Book not found exception"));
        Patron patron = patronRepository.findById(patronId).orElseThrow(()->new PatronNotFoundException("Patron not found exception"));

        if(!book.isAvailable()){
            throw new BookNotAvailableException("Book is not available");
        }

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(new Date());
        return borrowingRecordRepository.save(borrowingRecord);
    }

    public BorrowingRecord updateBorrowingRecord(Long bookId, Long patronId){
        Book book = bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException("Book not found exception"));
        Patron patron = patronRepository.findById(patronId).orElseThrow(()->new PatronNotFoundException("Patron not found exception"));
        BorrowingRecord borrowingRecord = borrowingRecordRepository.findLatestRecord(book,patron).orElseThrow(()->new BorrowingRecordNotAvailable("Borrowing record not found"));

        if(borrowingRecord.getReturnDate()!= null){
            throw new BorrowingRecordNotAvailable("Borrowing record not found");
        }

        return borrowingRecordRepository.save(borrowingRecord);
    }
}
