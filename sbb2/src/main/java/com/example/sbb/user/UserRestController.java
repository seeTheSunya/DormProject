package com.example.sbb.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserRestController {

    private final UserService userService;

    // 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserForm form) {
        try {
            userService.create(form.getUsername(), form.getPassword(), form.getEmail());
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserForm form) {
        Member member = userService.login(form.getUsername(), form.getPassword());
        
        if (member != null) {
            // 로그인 성공 시 사용자 이름(username)을 돌려줌
            return ResponseEntity.ok(Map.of("username", member.getUsername()));
        } else {
            // 로그인 실패 (401 Unauthorized 에러)
            return ResponseEntity.status(401).body("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    @Getter @Setter
    public static class UserForm {
        private String username;
        private String password;
        private String email;
    }
}