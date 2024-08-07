package com.Maids.LibraryManagementSystem.Repositories;

import com.Maids.LibraryManagementSystem.Models.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
}
