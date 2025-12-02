package com.example.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
    Member findByEmail(String email);
    Member findByUsernameAndEmail(String username, String email);
    boolean existsByUsername(String username);

    // ★ 추가: 인증 토큰으로 회원 찾기
    Member findByVerificationToken(String verificationToken);
}