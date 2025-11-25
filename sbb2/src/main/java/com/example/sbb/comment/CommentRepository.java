package com.example.sbb.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 "최상위 댓글(부모가 없는 댓글)"만 가져옴
    // (자식 댓글은 Entity 관계를 통해 가져옴)
    List<Comment> findByPostIdAndParentIsNullOrderByCreatedAtAsc(Long postId);
}