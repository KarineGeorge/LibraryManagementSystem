package com.Maids.LibraryManagementSystem.Controllers;

import com.Maids.LibraryManagementSystem.Exceptions.PatronIntegrityConstraintException;
import com.Maids.LibraryManagementSystem.Exceptions.PatronNotFoundException;
import com.Maids.LibraryManagementSystem.Models.Patron;
import com.Maids.LibraryManagementSystem.Services.PatronService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrons() {
        return new ResponseEntity<>(patronService.getAllPatrons(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatronById(@PathVariable Long id) {
        try {
            Patron patron = patronService.getPatronById(id);
            return ResponseEntity.ok(patron); }
        catch (PatronNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addPatron(@Valid @RequestBody Patron patron){
        try{
            return new ResponseEntity<>(patronService.addPatron(patron), HttpStatus.CREATED);

        }catch(PatronIntegrityConstraintException e){
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            errorDetails.put("cause", e.getCause() != null ? e.getCause().getMessage() : "No additional cause information");
            return ResponseEntity.badRequest().body(errorDetails);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatron(@PathVariable Long id) {
        try {
            patronService.deletePatronById(id);
            return ResponseEntity.ok().build();
        } catch (PatronNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatron(@PathVariable Long id, @Valid @RequestBody Patron patronDetails) {
        try{
            Patron updatedPatron = patronService.updatePatron(id, patronDetails);
            return new ResponseEntity<>(updatedPatron, HttpStatus.OK);
        }
        catch (PatronNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (PatronIntegrityConstraintException e) {

            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            errorDetails.put("cause", e.getCause() != null ? e.getCause().getMessage() : "No additional cause information");
            return ResponseEntity.badRequest().body(errorDetails);
        }
    }
}
