package com.example.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final MemberRepository memberRepository;

    // 아이디 중복 여부
    public boolean isUsernameDuplicate(String username) {
        return memberRepository.findByUsername(username) != null;
    }

    // 회원가입
    public Member create(String username, String password, String email) {

        if (memberRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("중복 아이디");
        }

        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);
        member.setEmail(email);
        member.setCreatedAt(LocalDateTime.now());
        // 처음 가입할 때는 임시비번 아님
        member.setTempPassword(false);

        return memberRepository.save(member);
    }

    // 로그인
    public Member login(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        if (member != null && member.getPassword().equals(password)) {
            return member;
        }
        return null;
    }

    // 비밀번호 재설정(임시 비밀번호 발급) - ❗메일 안 보내고, 그냥 리턴만
    public String resetPassword(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) return null;

        // 8자리 임시 비밀번호 생성
        String tempPw = UUID.randomUUID().toString().substring(0, 8);

        member.setPassword(tempPw);
        // 임시 비밀번호 사용 중 표시
        member.setTempPassword(true);

        memberRepository.save(member);
        return tempPw;  // 화면에 띄우기용으로 컨트롤러에 리턴
    }

    // 비밀번호 변경 (임시비번 → 새 비번)
    public boolean changePassword(String username, String currentPw, String newPw) {
        Member member = memberRepository.findByUsername(username);
        if (member == null) return false;

        // 현재 비밀번호(임시 비번 포함) 일치 여부 확인
        if (!member.getPassword().equals(currentPw)) {
            return false;
        }

        member.setPassword(newPw);
        // 더 이상 임시 비밀번호 아님
        member.setTempPassword(false);

        memberRepository.save(member);
        return true;
    }
}
