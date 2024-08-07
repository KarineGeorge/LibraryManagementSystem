package com.Maids.LibraryManagementSystem.Repositories;

import com.Maids.LibraryManagementSystem.Models.Book;
import com.Maids.LibraryManagementSystem.Models.BorrowingRecord;
import com.Maids.LibraryManagementSystem.Models.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    void deleteByBookId(Long id);
    void deleteByPatronId(Long id);

    @Query("Select B from BorrowingRecord B Where B.book = :book AND B.patron = :patron Order By B.borrowDate DESC Limit 1")
    Optional<BorrowingRecord> findLatestRecord(@Param("book") Book book, @Param("patron")Patron patron);
}
