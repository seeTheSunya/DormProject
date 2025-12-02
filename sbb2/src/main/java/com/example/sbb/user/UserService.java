package com.example.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final MemberRepository memberRepository;
    // ★ 추가: 이메일 발송 서비스 주입
    private final EmailService emailService;

    // 1. 회원가입 (수정됨: 인증 토큰 생성 및 메일 발송)
    public Member create(String username, String password, String email) {
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);
        member.setEmail(email);
        member.setVerified(false); // 초기엔 미인증 상태

        // ★ 1. 랜덤 인증 토큰 생성
        String token = UUID.randomUUID().toString();
        member.setVerificationToken(token);
        
        this.memberRepository.save(member);

        // ★ 2. 인증 메일 발송
        // (EmailService에서 제목과 내용을 알아서 만들어주므로, 토큰만 넘기면 됩니다)
        emailService.sendEmail(email, token);

        return member;
    }

    // 2. 로그인 (수정됨: 인증 여부 확인)
    public Member login(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        if (member != null && member.getPassword().equals(password)) {
            // ★ 인증된 회원인지 확인 (인증 안 됐으면 로그인 실패 처리)
            if (member.isVerified()) {
                return member;
            }
        }
        return null;
    }

    // ★ 3. 이메일 링크 클릭 시 인증 처리 (신규 추가)
    public boolean verifyUser(String token) {
        Member member = memberRepository.findByVerificationToken(token);
        if (member != null) {
            member.setVerified(true);
            member.setVerificationToken(null); // 토큰 삭제 (재사용 방지)
            memberRepository.save(member);
            return true;
        }
        return false;
    }
    
    // --- 기존 기능들 (유지) ---

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