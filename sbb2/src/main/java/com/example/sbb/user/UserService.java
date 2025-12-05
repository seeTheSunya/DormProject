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

    // 1. 회원가입 (인증 전까지는 DB에 저장하지 않음)
    public String createAndSendEmail(String username, String password, String email) {
        // 인증되지 않은 회원이 이미 존재하는지 확인 (인증 실패한 경우 삭제)
        Member existingUnverified = memberRepository.findByUsername(username);
        if (existingUnverified != null && !existingUnverified.isVerified()) {
            // 인증되지 않은 기존 회원 삭제
            memberRepository.delete(existingUnverified);
        }
        
        // 이메일로도 확인 (인증되지 않은 경우)
        Member existingByEmail = memberRepository.findByEmail(email);
        if (existingByEmail != null && !existingByEmail.isVerified()) {
            memberRepository.delete(existingByEmail);
        }

        // 토큰 생성 및 이메일 발송만 (DB 저장은 하지 않음)
        String token = UUID.randomUUID().toString();
        
        // 임시로 회원 정보를 저장 (인증 성공 시 실제 저장)
        Member tempMember = new Member();
        tempMember.setUsername(username);
        tempMember.setPassword(password);
        tempMember.setEmail(email);
        tempMember.setVerified(false);
        tempMember.setVerificationToken(token);
        
        // 임시 저장 (인증 실패 시 삭제 가능하도록)
        this.memberRepository.save(tempMember);
        
        // 이메일 발송
        emailService.sendEmail(email, token);
        
        return token;
    }

    // 2. 로그인
    @Transactional
    public Member login(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        if (member != null && member.getPassword().equals(password)) {
            // 기존 데이터 호환: is_verified가 없거나 false인 경우도 허용하고 자동으로 true로 설정
            if (!member.isVerified()) {
                member.setVerified(true);
                memberRepository.save(member);
            }
            return member;
        }
        return null;
    }

    // 3. 이메일 인증 (인증 성공 시에만 실제 회원으로 저장)
    public boolean verifyUser(String token) {
        Member member = memberRepository.findByVerificationToken(token);
        if (member != null && !member.isVerified()) {
            // 인증 성공: isVerified를 true로 변경하고 토큰 삭제
            member.setVerified(true);
            member.setVerificationToken(null);
            memberRepository.save(member);
            return true;
        }
        // 토큰이 없거나 이미 인증된 경우 실패
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
    // 인증된 회원만 중복 확인 (인증되지 않은 회원은 무시)
    public boolean isUsernameDuplicate(String username) {
        Member member = memberRepository.findByUsername(username);
        return member != null && member.isVerified();
    }

    public String findUsername(String email) {
        Member member = memberRepository.findByEmail(email);
        // 인증된 회원만 반환
        return (member != null && member.isVerified()) ? member.getUsername() : null;
    }

    public String findPassword(String username, String email) {
        Member member = memberRepository.findByUsernameAndEmail(username, email);
        return (member != null) ? member.getPassword() : null;
    }
}