package com.devprep.devprepai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;


    public Category(String name, User user) {
        this.name=name;
        this.user=user;
    }

    public Category() {

    }

    public Category(String trim, Optional<User> user) {
    }
}
