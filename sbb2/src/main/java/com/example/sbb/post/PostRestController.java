package com.example.sbb.post;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;

    // 목록
    @GetMapping
    public List<Post> list(@RequestParam(required = false) String category) {
        return postService.findAll(category);
    }

    // 상세
    @GetMapping("/{id}")
    public Post detail(@PathVariable Long id) {
        return postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
    }

    // 글쓰기
    @PostMapping
    public Post create(@RequestBody PostCreateRequest req) {
        return postService.createPost(
                req.getCategory(),
                req.getTitle(),
                req.getContent(),
                req.getUsername()
        );
    }

    // 좋아요
    @PostMapping("/{id}/like")
    public Map<String, Integer> like(@PathVariable Long id) {
        int likeCount = postService.increaseLike(id);
        return Map.of("likeCount", likeCount);
    }

    @Getter
    @Setter
    public static class PostCreateRequest {
        private String category;
        private String title;
        private String content;
        private String username;
    }
}
