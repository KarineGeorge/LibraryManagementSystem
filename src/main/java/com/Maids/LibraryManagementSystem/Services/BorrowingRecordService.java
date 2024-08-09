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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

    public BorrowingRecord borrowBook(Long bookId, Long patronId){
        Book book = bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException("Book not found exception"));
        Patron patron = patronRepository.findById(patronId).orElseThrow(()->new PatronNotFoundException("Patron not found exception"));

        if(!book.isAvailable()){
            throw new BookNotAvailableException("Book is already borrowed");
        }
        book.setAvailable(false);

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(new Timestamp(new Date().getTime()));
        return borrowingRecordRepository.save(borrowingRecord);
    }

    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long patronId){

        Book book = bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException("Book not found exception"));
        Patron patron = patronRepository.findById(patronId).orElseThrow(()->new PatronNotFoundException("Patron not found exception"));
        BorrowingRecord borrowingRecord = borrowingRecordRepository.findLatestRecord(book,patron).orElseThrow(()->new BorrowingRecordNotAvailable("Borrowing record not found"));
        borrowingRecord.setReturnDate(new Timestamp(new Date().getTime()));
        book.setAvailable(true);
        return borrowingRecordRepository.save(borrowingRecord);
    }
}
