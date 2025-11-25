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

    // 1. 목록 조회
    @GetMapping("/posts")
    public List<Post> getPosts(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "keyword", required = false) String keyword) {
        
        if (category != null && !category.isEmpty()) {
            if (keyword != null && !keyword.isEmpty()) {
                return postRepository.findByCategoryAndTitleContaining(category, keyword);
            }
            return postRepository.findByCategory(category);
        } else {
            return postRepository.findAll();
        }
    }

    // 2. 상세 조회 (★ 수정됨: name = "username" 추가)
    @GetMapping("/posts/{id}")
    public ResponseEntity<?> getPostById(
            @PathVariable("id") Long id, 
            @RequestParam(name = "username", required = false) String username) { // ★ 여기가 핵심입니다!
        
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

    // 4. 좋아요 토글
    @PostMapping("/posts/{id}/like")
    public ResponseEntity<Boolean> toggleLike(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        boolean isLiked = postService.toggleLike(id, body.get("username"));
        return ResponseEntity.ok(isLiked);
    }

    // 5. 인기글 조회
    @GetMapping("/posts/popular")
    public List<Post> getPopularPosts() {
        return postService.getPopularPosts();
    }

    @Getter @Setter
    public static class PostForm {
        private String title;
        private String content;
        private String category;
        private String username;
    }
}