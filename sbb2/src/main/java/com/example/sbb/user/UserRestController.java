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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserForm form) {
        try {
            userService.create(form.getUsername(), form.getPassword(), form.getEmail());
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserForm form) {
        Member member = userService.login(form.getUsername(), form.getPassword());
        if (member != null) {
            return ResponseEntity.ok(Map.of("username", member.getUsername()));
        } else {
            return ResponseEntity.status(401).body("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    // ★ 추가: 아이디 찾기 API
    @PostMapping("/find-id")
    public ResponseEntity<String> findId(@RequestBody Map<String, String> body) {
        String username = userService.findUsername(body.get("email"));
        if (username != null) {
            return ResponseEntity.ok(username);
        } else {
            return ResponseEntity.badRequest().body("해당 이메일로 등록된 계정이 없습니다.");
        }
    }

    // ★ 추가: 비밀번호 찾기 API
    @PostMapping("/find-password")
    public ResponseEntity<String> findPassword(@RequestBody Map<String, String> body) {
        String password = userService.findPassword(body.get("username"), body.get("email"));
        if (password != null) {
            return ResponseEntity.ok(password);
        } else {
            return ResponseEntity.badRequest().body("일치하는 회원 정보가 없습니다.");
        }
    }
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam(name = "username") String username) {
        boolean isDuplicate = userService.isUsernameDuplicate(username);
        return ResponseEntity.ok(isDuplicate); // 중복이면 true, 아니면 false 반환
    }

    @Getter @Setter
    public static class UserForm {
        private String username;
        private String password;
        private String email;
    }
}