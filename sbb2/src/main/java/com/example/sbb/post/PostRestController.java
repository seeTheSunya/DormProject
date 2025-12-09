package com.example.sbb.post;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PostRestController {

    private final PostRepository postRepository;
    private final PostService postService;

    // 1. 목록 조회 (학교별 필터링 추가)
    @GetMapping("/posts")
    public List<Post> getPosts(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "school", required = false) String school) {
        
        // 학교 정보가 없으면 기존 방식으로 동작 (하위 호환성)
        if (school == null || school.isEmpty()) {
            if (category != null && !category.isEmpty()) {
                if (keyword != null && !keyword.isEmpty()) {
                    return postRepository.findByCategoryAndTitleContaining(category, keyword);
                }
                return postRepository.findByCategory(category);
            } else {
                return postRepository.findAll();
            }
        }
        
        // 학교별 필터링 적용
        if (category != null && !category.isEmpty()) {
            if (keyword != null && !keyword.isEmpty()) {
                return postRepository.findByCategoryAndMember_SchoolAndTitleContaining(category, school, keyword);
            }
            return postRepository.findByCategoryAndMember_School(category, school);
        } else {
            return postRepository.findByMember_School(school);
        }
    }

    // 2. 상세 조회
    @GetMapping("/posts/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") Long id, @RequestParam(name = "username", required = false) String username) {
        return postRepository.findById(id)
                .map(post -> {
                    boolean liked = false;
                    if (username != null) {
                        liked = postService.isLiked(id, username);
                    }
                    return ResponseEntity.ok(Map.of("post", post, "liked", liked));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. 글 작성
    @PostMapping("/posts")
    public void createPost(@RequestBody PostForm postForm) {
        postService.create(postForm.getTitle(), postForm.getContent(), postForm.getCategory(), postForm.getUsername());
    }

    // ★ 4. 좋아요 토글 (수정됨: 에러 메시지 반환)
    @PostMapping("/posts/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        try {
            boolean isLiked = postService.toggleLike(id, body.get("username"));
            return ResponseEntity.ok(isLiked);
        } catch (IllegalArgumentException e) {
            // "존재하지 않는 사용자입니다" 메시지를 프론트엔드로 보냄 (400 Bad Request)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5. 인기글 조회
    @GetMapping("/posts/popular")
    public List<Post> getPopularPosts() {
        return postService.getPopularPosts();
    }

    // 6. 글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        try {
            postService.delete(id, body.get("username"));
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("삭제 중 오류가 발생했습니다.");
        }
    }

    @Getter @Setter
    public static class PostForm {
        private String title;
        private String content;
        private String category;
        private String username;
    }
}