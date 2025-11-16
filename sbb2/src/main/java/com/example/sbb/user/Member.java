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

    @Column(unique = true)
    private String email;

    private LocalDateTime createdAt;

    // 자동으로 가입 시간 저장
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // === Getter ===
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // === Setter (여기에 setCreatedAt이 추가되었습니다) ===
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    
    // ★ 이 부분이 없어서 에러가 났습니다. 추가!
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}