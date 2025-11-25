package com.example.sbb.post;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.sbb.user.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String category; // group, review, recipe, tip, counseling

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

    // ❤️ 좋아요 기능
    @Column(nullable = false)
    private int likeCount = 0;

    // ✏ 작성자 (문자열로 저장)
    @Column(length = 50)
    private String writer;

    @ManyToOne
    private Member member;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
