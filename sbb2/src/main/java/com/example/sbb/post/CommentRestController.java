package com.example.sbb.post;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CommentRequest req) {
        try {
            Comment comment = commentService.addComment(
                    req.getPostId(),
                    req.getUsername(),
                    req.getContent()
            );
            return ResponseEntity.ok(comment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 프론트에서 받을 JSON 형태
    @Getter
    public static class CommentRequest {
        private Long postId;      // 어느 게시글인지
        private String username;  // 작성자 아이디
        private String content;   // 댓글 내용
    }
}
