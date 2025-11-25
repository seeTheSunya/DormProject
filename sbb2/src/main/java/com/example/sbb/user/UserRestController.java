package com.example.sbb.user;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserRestController {

    private final UserService userService;

    // ===============================
    // 1. 아이디 중복 확인
    // ===============================
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userService.isUsernameDuplicate(username);
        return ResponseEntity.ok(exists);   // true면 이미 존재
    }

    // ===============================
    // 2. 회원가입
    // ===============================
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserForm form) {

        // 비밀번호 확인 체크
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        try {
            userService.create(form.getUsername(), form.getPassword(), form.getEmail());
            return ResponseEntity.ok("회원가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }
    }

    // ===============================
    // 3. 로그인
    // ===============================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserForm form) {
        Member member = userService.login(form.getUsername(), form.getPassword());

        if (member != null) {
            return ResponseEntity.ok(
                    Map.of(
                            "username", member.getUsername(),
                            // Member 엔티티에 boolean tempPassword 필드 + Lombok @Getter 필요
                            "mustChangePassword", member.isTempPassword()
                    )
            );
        } else {
            return ResponseEntity.status(401).body("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    // ===============================
    // 4. 비밀번호 찾기 (임시 비밀번호 발급)
    // ===============================
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String newPw = userService.resetPassword(email);

        if (newPw == null) {
            return ResponseEntity.badRequest().body("해당 이메일의 회원이 없습니다.");
        }

        // 메일 전송 없이, 프론트에 임시 비번 직접 전달
        return ResponseEntity.ok(Map.of("newPassword", newPw));
    }

    // ===============================
    // 5. 비밀번호 변경 (임시 비번 → 새 비번)
    // ===============================
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest req) {
        boolean ok = userService.changePassword(
                req.getUsername(),
                req.getCurrentPassword(),   // 현재 비번(임시 비번 포함)
                req.getNewPassword()        // 새 비번
        );

        if (!ok) {
            return ResponseEntity.badRequest().body("비밀번호 변경 실패: 현재 비밀번호를 확인해주세요.");
        }

        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    // ===============================
    // DTO 영역
    // ===============================
    @Getter
    @Setter
    public static class UserForm {
        private String username;
        private String password;
        private String passwordConfirm;
        private String email;
    }

    @Getter
    @Setter
    public static class ChangePasswordRequest {
        private String username;
        private String currentPassword; // 임시 비번 or 기존 비번
        private String newPassword;     // 새 비번
    }
}
