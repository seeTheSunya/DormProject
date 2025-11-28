package com.example.sbb.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

    // 1. 글 작성
    public void create(String title, String content, String category, String username) {
        Post post = new Post();
        post.setCategory(category);
        post.setTitle(title);
        post.setContent(content);
        post.setWriter(username);
        post.setCreatedAt(LocalDateTime.now());
        post.setLikeCount(0);

        if (username != null && !username.isEmpty()) {
            Member member = memberRepository.findByUsername(username);
            if (member != null) {
                post.setMember(member);
            }
        }
        this.postRepository.save(post);
    }

    // ★ 2. 좋아요 토글 (수정됨: 회원 확인 로직 추가)
    @Transactional
    public boolean toggleLike(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        
        Member member = memberRepository.findByUsername(username);

        // ★★★ [추가된 안전장치] ★★★
        // 회원이 조회되지 않으면(null이면) 에러를 내거나 false를 반환해서 DB 저장을 막아야 합니다.
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다. 다시 로그인해주세요.");
        }

        Optional<PostLike> like = postLikeRepository.findByPostAndMember(post, member);

        if (like.isPresent()) {
            postLikeRepository.delete(like.get());
            post.setLikeCount(post.getLikeCount() - 1);
            postRepository.save(post);
            return false; 
        } else {
            PostLike newLike = new PostLike();
            newLike.setPost(post);
            newLike.setMember(member); // 여기서 member가 null이면 안 됨!
            postLikeRepository.save(newLike);
            post.setLikeCount(post.getLikeCount() + 1);
            postRepository.save(post);
            return true; 
        }
    }

    // 3. 좋아요 여부 확인
    public boolean isLiked(Long postId, String username) {
        if (username == null) return false;
        Member member = memberRepository.findByUsername(username);
        // ★ 회원이 없으면 false 반환
        if (member == null) return false;
        
        Post post = postRepository.findById(postId).orElseThrow();
        return postLikeRepository.existsByPostAndMember(post, member);
    }

    // 4. 인기글 조회
    public List<Post> getPopularPosts() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        return postRepository.findTop3ByCreatedAtAfterOrderByLikeCountDesc(oneWeekAgo);
    }
}