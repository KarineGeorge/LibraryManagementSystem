package com.Maids.LibraryManagementSystem.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Patron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Patron name is required")
    private String name;

    @Column(unique = true)
    @Email(message = "Enter a valid email")
    @NotNull(message = "Patron email is required")
    private String email;

    @Column(unique = true)
    @NotBlank(message = "Enter a valid telephone number")
    private String telephone;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace")
    private String password;

    public Patron(Long id, String name, String email, String telephone, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
    }

    public Patron() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
