package com.example.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 필수
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;

    // ... (기존 create, login, verifyUser 메서드 유지) ...
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

    public Member login(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        if (member != null && member.getPassword().equals(password)) {
            if (member.isVerified()) return member;
        }
        return null;
    }

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

    // ★★★ [추가] 회원 정보 수정 (아이디, 비밀번호) ★★★
    @Transactional
    public Member update(String currentUsername, String currentPassword, String newUsername, String newPassword) {
        Member member = memberRepository.findByUsername(currentUsername);
        
        // 1. 현재 비밀번호 확인
        if (member == null || !member.getPassword().equals(currentPassword)) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 2. 아이디 변경 시 중복 체크
        if (!currentUsername.equals(newUsername)) {
            if (memberRepository.existsByUsername(newUsername)) {
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            }
            member.setUsername(newUsername);
        }

        // 3. 새 비밀번호가 있으면 변경
        if (newPassword != null && !newPassword.isEmpty()) {
            member.setPassword(newPassword);
        }

        return memberRepository.save(member);
    }

    // ... (기존 delete, updateTheme 등 나머지 메서드 유지) ...
    public boolean delete(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        if (member != null && member.getPassword().equals(password)) {
            memberRepository.delete(member);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateTheme(String username, String theme) {
        Member member = memberRepository.findByUsername(username);
        if (member != null) {
            member.setTheme(theme);
            memberRepository.save(member);
            return true;
        }
        return false;
    }

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