package com.example.sbb.post;


import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 필요한 경우 제목으로 검색 등의 메서드 추가 가능
}