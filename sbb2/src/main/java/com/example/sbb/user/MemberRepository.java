package com.example.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.QueryHint;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @QueryHints({@QueryHint(name = "jakarta.persistence.query.timeout", value = "5000")})
    @Query("SELECT m FROM Member m WHERE m.username = :username")
    Member findByUsername(@Param("username") String username);
    
    @QueryHints({@QueryHint(name = "jakarta.persistence.query.timeout", value = "5000")})
    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Member findByEmail(@Param("email") String email);
    
    @QueryHints({@QueryHint(name = "jakarta.persistence.query.timeout", value = "5000")})
    @Query("SELECT m FROM Member m WHERE m.username = :username AND m.email = :email")
    Member findByUsernameAndEmail(@Param("username") String username, @Param("email") String email);
    
    @QueryHints({@QueryHint(name = "jakarta.persistence.query.timeout", value = "5000")})
    @Query("SELECT COUNT(m) > 0 FROM Member m WHERE m.username = :username")
    boolean existsByUsername(@Param("username") String username);

    // ★ 추가: 인증 토큰으로 회원 찾기
    @QueryHints({@QueryHint(name = "jakarta.persistence.query.timeout", value = "5000")})
    @Query("SELECT m FROM Member m WHERE m.verificationToken = :token")
    Member findByVerificationToken(@Param("token") String verificationToken);
}