package com.Maids.LibraryManagementSystem.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "Patrons")
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

    @Column()
    @NotBlank(message = "Enter a valid telephone number")
    private String telephone;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Timestamp createdAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = true)
    private Timestamp updatedAt;

    public Patron(Long id, String name, String email, String telephone, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
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

}
