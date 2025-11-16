package com.example.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final MemberRepository memberRepository;

    // 회원가입
    public Member create(String username, String password, String email) {
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password); // 실무에선 암호화 필수! (지금은 학습용이라 평문 저장)
        member.setEmail(email);
        member.setCreatedAt(java.time.LocalDateTime.now());
        return this.memberRepository.save(member);
    }

    // 로그인 (성공하면 Member 객체 리턴, 실패하면 null)
    public Member login(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        // 아이디가 존재하고, 비밀번호가 일치하는지 확인
        if (member != null && member.getPassword().equals(password)) {
            return member;
        }
        return null;
    }
}