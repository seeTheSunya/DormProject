package com.example.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
    Member findByEmail(String email);
    Member findByUsernameAndEmail(String username, String email);
    
    // ★ 추가: 아이디 존재 여부 확인 (있으면 true, 없으면 false 반환)
    boolean existsByUsername(String username);
}