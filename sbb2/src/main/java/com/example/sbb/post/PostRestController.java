package com.example.sbb.post;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import org.springframework.http.ResponseEntity; // 1. import 추가

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PostRestController {

    private final PostRepository postRepository;
    private final PostService postService;

    // 1. 목록 조회 (카테고리별/전체)
    @GetMapping("/posts")
    public List<Post> getPosts(@RequestParam(name = "category", required = false) String category) {
        
        if (category != null && !category.isEmpty()) {
            return postRepository.findByCategory(category);
        } else {
            return postRepository.findAll();
        }
    }

    // 2. ★★★ [추가] 상세 조회 ★★★
    // (예: /api/posts/1)
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") Long id) {
        // ID로 Post를 찾고, 있으면 Post 객체를, 없으면 404 Not Found 에러를 반환
        return postRepository.findById(id)
                .map(post -> ResponseEntity.ok(post)) // 찾았으면 post 객체 반환
                .orElse(ResponseEntity.notFound().build()); // 못 찾았으면 404 반환
    }

    // 3. 글 작성
    @PostMapping("/posts")
    public void createPost(@RequestBody PostForm postForm) {
        postService.create(
            postForm.getTitle(), 
            postForm.getContent(), 
            postForm.getCategory(), 
            postForm.getUsername()
        );
    }

    @Getter @Setter
    public static class PostForm {
        private String title;
        private String content;
        private String category;
        private String username;
    }
}