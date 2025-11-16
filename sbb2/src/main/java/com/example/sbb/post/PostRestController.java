package com.example.sbb.post;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PostRestController {

    private final PostRepository postRepository;
    private final PostService postService;

    // 1. 조회
    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 2. ★ 글 작성 (이 부분이 있어야 글쓰기가 됩니다!)
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