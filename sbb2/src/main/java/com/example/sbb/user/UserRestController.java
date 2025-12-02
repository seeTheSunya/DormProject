package com.example.sbb.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.io.IOException;
import java.util.Map;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserRestController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserForm form) {
        // ★ 추가: 비밀번호 일치 여부 확인
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        try {
            userService.create(form.getUsername(), form.getPassword(), form.getEmail());
            return ResponseEntity.ok("회원가입 완료! 이메일 인증을 진행해주세요.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }

    // 이메일 인증 링크 처리 (GET 방식)
    @GetMapping("/verify")
    public void verifyEmail(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        boolean verified = userService.verifyUser(token);
        if (verified) {
            // 인증 성공 시 로그인 페이지로 이동
            response.sendRedirect("/login.html?verified=true");
        } else {
            // 인증 실패 시 에러 페이지 또는 메시지
            response.sendError(400, "유효하지 않은 인증 링크입니다.");
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserForm form) {
        Member member = userService.login(form.getUsername(), form.getPassword());
        
        if (member != null) {
            return ResponseEntity.ok(Map.of("username", member.getUsername()));
        } else {
            return ResponseEntity.status(401).body("아이디/비번이 틀렸거나, 이메일 인증이 완료되지 않았습니다.");
        }
    }

    @PostMapping("/find-id")
    public ResponseEntity<String> findId(@RequestBody Map<String, String> body) {
        String username = userService.findUsername(body.get("email"));
        return (username != null) ? ResponseEntity.ok(username) : ResponseEntity.badRequest().body("계정 없음");
    }

    @PostMapping("/find-password")
    public ResponseEntity<String> findPassword(@RequestBody Map<String, String> body) {
        String password = userService.findPassword(body.get("username"), body.get("email"));
        return (password != null) ? ResponseEntity.ok(password) : ResponseEntity.badRequest().body("정보 불일치");
    }
    
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam(name = "username") String username) {
        return ResponseEntity.ok(userService.isUsernameDuplicate(username));
    }

    @Getter @Setter
    public static class UserForm {
        private String username;
        private String password;
        private String passwordConfirm; // ★ 복구: 비밀번호 확인용 필드
        private String email;
    }

    // ★ 복구: 비밀번호 변경 요청 DTO (추후 기능 확장 대비)
    @Getter
    @Setter
    public static class ChangePasswordRequest {
        private String username;
        private String currentPassword; // 임시 비번 or 기존 비번
        private String newPassword;     // 새 비번
    }
}