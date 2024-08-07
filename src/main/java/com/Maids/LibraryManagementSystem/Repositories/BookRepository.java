package com.Maids.LibraryManagementSystem.Repositories;

import com.Maids.LibraryManagementSystem.Models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}


