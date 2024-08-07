package com.Maids.LibraryManagementSystem.Services;

import com.Maids.LibraryManagementSystem.Exceptions.BookNotFoundException;
import com.Maids.LibraryManagementSystem.Exceptions.PatronIntegrityConstraintException;
import com.Maids.LibraryManagementSystem.Exceptions.PatronNotFoundException;
import com.Maids.LibraryManagementSystem.Models.Patron;
import com.Maids.LibraryManagementSystem.Repositories.BorrowingRecordRepository;
import com.Maids.LibraryManagementSystem.Repositories.PatronRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.PatternSyntaxException;

@Service
public class PatronService {

    private final PatronRepository patronRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    public PatronService(PatronRepository patronRepository, BorrowingRecordRepository borrowingRecordRepository) {
        this.patronRepository = patronRepository;
        this.borrowingRecordRepository = borrowingRecordRepository;
    }

    public List<Patron> getAllPatrons(){
        return patronRepository.findAll();
    }

    public Patron getPatronById(Long id){
        return patronRepository.findById(id).orElseThrow(()->new PatronNotFoundException("Patron Not Found Exception"));
    }

    public Patron addPatron(Patron patron){
        try{
            return patronRepository.save(patron);
        }catch(DataIntegrityViolationException e){
            throw new PatronIntegrityConstraintException("Failed to create the patron due to integrity constraints.",e);
        }
    }

    public Patron updatePatron(Long id, Patron patron){
        try{
            Patron oldPatron = getPatronById(id);
            oldPatron.setEmail(patron.getEmail());
            oldPatron.setPassword(patron.getPassword());
            oldPatron.setName(patron.getName());
            oldPatron.setTelephone(patron.getTelephone());
            return patronRepository.save(patron);
        }catch(DataIntegrityViolationException e){
            throw new PatronIntegrityConstraintException("Failed to create the patron due to integrity constraints.",e);
        }
    }

    @Transactional
    public void deletePatronById(Long id){
        if (!patronRepository.existsById(id)) {
            throw new PatronNotFoundException("Patron not found");
        }
        borrowingRecordRepository.deleteByPatronId(id);
        patronRepository.deleteById(id);
    }
}
