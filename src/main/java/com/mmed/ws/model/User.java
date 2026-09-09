package com.mmed.ws.model;

import com.mmed.ws.util.MyUtil;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User(UUID id, String email, String password, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public User(String email, String password, LocalDateTime createdAt) {
        this(MyUtil.generateId(), email, password, createdAt);
    }

    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getPassword() {
        return password;
    }
}
