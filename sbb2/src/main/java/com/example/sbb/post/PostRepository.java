package com.example.sbb.post;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Long> {
    
    List<Post> findByCategory(String category);
    List<Post> findByCategoryAndTitleContaining(String category, String keyword);

    // ★ 추가: 실시간 인기글 조회
    // 조건: 작성일이 특정 날짜(일주일 전) 이후인 글 중에서, 좋아요 순으로 상위 3개
    List<Post> findTop3ByCreatedAtAfterOrderByLikeCountDesc(LocalDateTime date);
}