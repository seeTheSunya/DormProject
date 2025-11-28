package com.example.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final MemberRepository memberRepository;

    public Member create(String username, String password, String email) {
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password); 
        member.setEmail(email);
        member.setCreatedAt(java.time.LocalDateTime.now());
        return this.memberRepository.save(member);
    }

    public Member login(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        if (member != null && member.getPassword().equals(password)) {
            return member;
        }
        return null;
    }

    // ★ 추가: 아이디 찾기 서비스
    public String findUsername(String email) {
        Member member = memberRepository.findByEmail(email);
        return (member != null) ? member.getUsername() : null;
    }

    // ★ 추가: 비밀번호 찾기 서비스
    // (실무에서는 이메일로 임시 비번을 보내야 하지만, 지금은 학습용이라 직접 알려줍니다)
    public String findPassword(String username, String email) {
        Member member = memberRepository.findByUsernameAndEmail(username, email);
        return (member != null) ? member.getPassword() : null;
    }
    public boolean isUsernameDuplicate(String username) {
        return memberRepository.existsByUsername(username);
    }
}