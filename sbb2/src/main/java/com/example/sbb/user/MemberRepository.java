package com.example.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 로그인 처리를 위해 username으로 회원 찾기 기능
    Member findByUsername(String username);
}