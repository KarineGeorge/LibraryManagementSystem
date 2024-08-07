package com.Maids.LibraryManagementSystem.Repositories;

import com.Maids.LibraryManagementSystem.Models.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Long> {

}
