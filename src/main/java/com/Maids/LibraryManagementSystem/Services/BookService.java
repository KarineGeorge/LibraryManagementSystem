package com.Maids.LibraryManagementSystem.Services;

import com.Maids.LibraryManagementSystem.Exceptions.BookIntegrityConstraintException;
import com.Maids.LibraryManagementSystem.Exceptions.BookNotFoundException;
import com.Maids.LibraryManagementSystem.Models.Book;
import com.Maids.LibraryManagementSystem.Repositories.BookRepository;
import com.Maids.LibraryManagementSystem.Repositories.BorrowingRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    @Autowired
    public BookService(BookRepository bookRepository, BorrowingRecordRepository borrowingRecordRepository){
        this.bookRepository = bookRepository;
        this.borrowingRecordRepository = borrowingRecordRepository;
    }
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book getBookById(Long id){
        return bookRepository.findById(id).orElseThrow(()->new BookNotFoundException("Book Not Found Exception"));
    }

    public Book addBook(Book book){
        try {
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new BookIntegrityConstraintException("Failed to create the book due to integrity constraints.", e);
        }
    }

    public Book updateBook(Long id,Book book){
        try {
            Book oldBook = getBookById(id);
            oldBook.setAuthor(book.getAuthor());
            oldBook.setIsbn(book.getIsbn());
            oldBook.setTitle(book.getTitle());
            oldBook.setPublicationYear(book.getPublicationYear());
            return bookRepository.save(oldBook);

        } catch(DataIntegrityViolationException e){
            throw new BookIntegrityConstraintException("Failed to Update the book due to integrity constraints.", e);
        }
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found");
        }
        borrowingRecordRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }
}
