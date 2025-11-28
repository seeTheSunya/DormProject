package com.example.sbb.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private LocalDateTime createdAt;

    // 🔥 새로 추가: 임시 비밀번호 여부
    @Column(nullable = false)
    private boolean tempPassword = false;

    // ===== Getter =====
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isTempPassword() {
        return tempPassword;
    }

    // ===== Setter =====
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setTempPassword(boolean tempPassword) {
        this.tempPassword = tempPassword;
    }
}
