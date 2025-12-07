package com.example.sbb.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member", indexes = {
    @Index(name = "idx_member_username", columnList = "username"),
    @Index(name = "idx_member_email", columnList = "email"),
    @Index(name = "idx_member_verification_token", columnList = "verification_token")
})
public class Member {

	private String theme = "light";

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String school; // 학교 코드 (예: "chungbuk", "yonsei")

    private LocalDateTime createdAt;

    // ★ 추가: 이메일 인증 여부 (기본값 false)
    private boolean isVerified = false;

    // ★ 추가: 인증 토큰 (랜덤 문자열 저장)
    private String verificationToken;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getSchool() { return school; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isVerified() { return isVerified; }
    public String getVerificationToken() { return verificationToken; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setSchool(String school) { this.school = school; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setVerified(boolean verified) { isVerified = verified; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }
}