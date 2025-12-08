package com.example.sbb.comment;

import com.example.sbb.post.Post;
import com.example.sbb.user.Member;
import com.fasterxml.jackson.annotation.JsonIgnore; // 1. 이 import가 필수입니다!
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore // 2. [필수 수정] 게시글 정보를 무한히 불러오지 않도록 차단
    private Post post;

    @ManyToOne
    @JoinColumn(name = "author_id")
    // 참고: 만약 Member 쪽에도 'List<Comment>'가 있다면 여기도 @JsonIgnore를 붙여야 합니다.
    // 일단은 놔두고, 에러가 계속되면 여기도 붙이세요.
    private Member author;

    // ★ 대댓글 기능의 핵심: 부모 댓글
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore // 3. [필수 수정] 자식이 부모를 부르고 부모가 자식을 부르는 루프 차단
    private Comment parent;

    // ★ 자식 댓글들 (OneToMany)
    // 주의: 여기에는 @JsonIgnore를 붙이면 안 됩니다! (대댓글 리스트는 가져와야 하니까요)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> children = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}