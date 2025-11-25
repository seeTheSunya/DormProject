package com.example.sbb.post;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PostRestController {

    private final PostRepository postRepository;
    private final PostService postService;

    // 1. 목록 조회 (검색 기능 추가됨)
    @GetMapping("/posts")
    public List<Post> getPosts(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "keyword", required = false) String keyword) { // ★ keyword 추가
        
        if (category != null && !category.isEmpty()) {
            // 검색어가 있으면 -> 카테고리 + 제목 검색
            if (keyword != null && !keyword.isEmpty()) {
                return postRepository.findByCategoryAndTitleContaining(category, keyword);
            }
            // 검색어가 없으면 -> 카테고리 전체 조회
            return postRepository.findByCategory(category);
        } else {
            return postRepository.findAll();
        }
    }

    // 2. 상세 조회
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") Long id) {
        return postRepository.findById(id)
                .map(post -> ResponseEntity.ok(post))
                .orElse(ResponseEntity.notFound().build());
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