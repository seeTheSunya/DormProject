package com.example.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 아이디로 회원 찾기
    Member findByUsername(String username);

    // 이메일로 회원 찾기 (비밀번호 찾기용)
    Member findByEmail(String email);
}
