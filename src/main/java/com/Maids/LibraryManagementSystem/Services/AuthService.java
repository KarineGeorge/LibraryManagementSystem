package com.Maids.LibraryManagementSystem.Services;
import com.Maids.LibraryManagementSystem.Models.Librarian;
import com.Maids.LibraryManagementSystem.Repositories.LibrarianRepository;
import com.Maids.LibraryManagementSystem.dtos.LoginUserDto;
import com.Maids.LibraryManagementSystem.dtos.RegisterUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final LibrarianRepository librarianRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(
            LibrarianRepository librarianRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.librarianRepository = librarianRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public Librarian signup(RegisterUserDto input){
        Librarian librarian = new Librarian();
        librarian.setName(input.getName());
        librarian.setEmail(input.getEmail());
        librarian.setPassword(passwordEncoder.encode(input.getPassword()));

        return librarianRepository.save(librarian);
    }

    public Librarian authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return librarianRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
