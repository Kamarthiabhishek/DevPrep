package com.devprep.devprepai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message ="Name is required")
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message ="Email is required")
    @Email
    private String email;

    @Column(nullable = false)
    @NotBlank(message ="Password is required")
    private String password;

    @Column(nullable = false)
    @NotBlank
    private String role;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy ="user")
    private List<Category> categories = new ArrayList<>();

    public User() {
    }

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
