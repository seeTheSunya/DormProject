package com.example.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ★ 트랜잭션 처리를 위해 추가

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;

    // 1. 회원가입
    public Member create(String username, String password, String email) {
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);
        member.setEmail(email);
        member.setVerified(false);

        String token = UUID.randomUUID().toString();
        member.setVerificationToken(token);
        
        this.memberRepository.save(member);
        emailService.sendEmail(email, token);

        return member;
    }

    // 2. 로그인
    public Member login(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        if (member != null && member.getPassword().equals(password)) {
            if (member.isVerified()) {
                return member;
            }
        }
        return null;
    }

    // 3. 이메일 인증
    public boolean verifyUser(String token) {
        Member member = memberRepository.findByVerificationToken(token);
        if (member != null) {
            member.setVerified(true);
            member.setVerificationToken(null);
            memberRepository.save(member);
            return true;
        }
        return false;
    }

    // 4. 회원 탈퇴
    public boolean delete(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        // 사용자가 존재하고 비밀번호가 일치하면 삭제
        if (member != null && member.getPassword().equals(password)) {
            memberRepository.delete(member);
            return true;
        }
        return false;
    }

    // ★ 5. 테마 변경 (새로 추가된 부분)
    @Transactional
    public boolean updateTheme(String username, String theme) {
        Member member = memberRepository.findByUsername(username);
        if (member != null) {
            member.setTheme(theme); // Member 엔티티에 setTheme 메서드가 있어야 합니다.
            memberRepository.save(member);
            return true;
        }
        return false;
    }
    
    // --- 기존 기능들 ---
    public boolean isUsernameDuplicate(String username) {
        return memberRepository.existsByUsername(username);
    }

    public String findUsername(String email) {
        Member member = memberRepository.findByEmail(email);
        return (member != null) ? member.getUsername() : null;
    }

    public String findPassword(String username, String email) {
        Member member = memberRepository.findByUsernameAndEmail(username, email);
        return (member != null) ? member.getPassword() : null;
    }
}