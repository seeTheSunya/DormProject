package com.example.sbb.post;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.sbb.user.Member;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(length = 50, nullable = false)
    private String category;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime createdAt;

    private String imageUrl;

    @ManyToOne
    private Member member;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // === Getters (getId()가 public이어야 합니다) ===
    public Long getId() { return id; }
    public String getCategory() { return category; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getImageUrl() { return imageUrl; }
    public Member getMember() { return member; }

    // === Setters ===
    public void setId(Long id) { this.id = id; }
    public void setCategory(String category) { this.category = category; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setMember(Member member) { this.member = member; }
}