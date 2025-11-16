package com.example.sbb.post; // 패키지명 수정

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.sbb.user.Member; // Member 클래스 import 필요

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ★ 카테고리 필드 추가 (게시판 종류 구분용)
    // 예: "free"(자유), "qna"(질문), "notice"(공지)
    @Column(length = 50, nullable = false)
    private String category;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime createdAt;

    // 이미지 경로 저장 필드
    private String imageUrl;

    // 작성자 (N:1 관계)
    @ManyToOne
    private Member member;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // === Getter, Setter (여기 있는 걸 다 복사하세요) ===
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    // ★ 이 부분이 없어서 에러가 났던 것입니다. 추가해주세요!
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}