package com.example.sbb.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 전체 / 카테고리별 조회
    public List<Post> findAll(String category) {
        if (category == null || category.equals("all")) {
            return postRepository.findAll();
        }
        return postRepository.findByCategory(category);
    }

    // 게시글 하나 조회
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    // 게시글 등록
    public Post createPost(String category, String title, String content, String username) {
        Post post = new Post();
        post.setCategory(category);
        post.setTitle(title);
        post.setContent(content);
        post.setWriter(username);
        post.setCreatedAt(LocalDateTime.now());
        post.setLikeCount(0);

        return postRepository.save(post);
    }

    // 좋아요 처리
    public int increaseLike(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow();
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
        return post.getLikeCount();
    }
}
