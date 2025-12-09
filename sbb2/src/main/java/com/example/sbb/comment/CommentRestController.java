package com.example.sbb.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/api/comments")
@RequiredArgsConstructor
@RestController
public class CommentRestController {

    private final CommentService commentService;

    // 1. 댓글 목록 조회 (게시글 ID로)
    @GetMapping("/{postId}")
    public List<CommentDto> getComments(@PathVariable("postId") Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 2. 댓글 작성 (대댓글 포함)
    @PostMapping("/{postId}")
    public void createComment(@PathVariable("postId") Long postId, @RequestBody CommentForm form) {
        commentService.create(postId, form.getContent(), form.getUsername(), form.getParentId());
    }

    // 3. 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId, @RequestBody Map<String, String> body) {
        try {
            commentService.delete(commentId, body.get("username"));
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("삭제 중 오류가 발생했습니다.");
        }
    }

    // Entity -> DTO 변환 (무한 재귀 방지 및 데이터 구조화)
    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthorName(comment.getAuthor() != null ? comment.getAuthor().getUsername() : "익명");
        dto.setCreatedAt(comment.getCreatedAt());
        // 자식 댓글들도 재귀적으로 변환
        dto.setChildren(comment.getChildren().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
        return dto;
    }

    // DTO 클래스들
    @Getter @Setter
    public static class CommentForm {
        private String content;
        private String username;
        private Long parentId; // 대댓글일 경우 부모 ID
    }

    @Getter @Setter
    public static class CommentDto {
        private Long id;
        private String content;
        private String authorName;
        private LocalDateTime createdAt;
        private List<CommentDto> children; // 대댓글 리스트
    }
}
