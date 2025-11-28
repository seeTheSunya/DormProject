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

    // ★★★ [추가됨] 좋아요 개수 저장 필드 ★★★
    // DB의 'like_count' 컬럼과 매핑됩니다.
    // null 방지를 위해 기본값을 0으로 설정합니다.
    @Column(nullable = false)
    private Integer likeCount = 0;

    @ManyToOne
    private Member member;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        // 저장되기 전에 likeCount가 없으면 0으로 채워줍니다.
        if (this.likeCount == null) {
            this.likeCount = 0;
        }
    }

    // === Getters ===
    public Long getId() { return id; }
    public String getCategory() { return category; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getImageUrl() { return imageUrl; }
    public Member getMember() { return member; }
    public Integer getLikeCount() { return likeCount; } // ★ Getter 추가

    // === Setters ===
    public void setId(Long id) { this.id = id; }
    public void setCategory(String category) { this.category = category; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setMember(Member member) { this.member = member; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; } // ★ Setter 추가
}