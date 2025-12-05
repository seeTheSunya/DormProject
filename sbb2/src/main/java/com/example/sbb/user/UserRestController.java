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

    // ... (기존 register, verifyEmail, login 메서드 유지) ...
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserForm form) {
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }
        if (userService.isUsernameDuplicate(form.getUsername())) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }
        if (userService.findUsername(form.getEmail()) != null) {
            return ResponseEntity.badRequest().body("이미 등록된 이메일입니다.");
        }
        try {
            userService.create(form.getUsername(), form.getPassword(), form.getEmail());
            return ResponseEntity.ok("이메일을 발송했습니다. 회원가입을 위해 인증해주세요.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }

    @GetMapping("/verify")
    public void verifyEmail(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        boolean verified = userService.verifyUser(token);
        if (verified) {
            response.sendRedirect("/login.html?verified=true");
        } else {
            response.sendError(400, "유효하지 않은 인증 링크입니다.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserForm form) {
        Member member = userService.login(form.getUsername(), form.getPassword());
        if (member != null) {
            return ResponseEntity.ok(Map.of(
                "username", member.getUsername(),
                "theme", member.getTheme() != null ? member.getTheme() : "light"
            ));
        } else {
            return ResponseEntity.status(401).body("아이디/비번이 틀렸거나, 이메일 인증이 완료되지 않았습니다.");
        }
    }

    // ★★★ [추가] 회원 정보 수정 API ★★★
    @PostMapping("/update")
    public ResponseEntity<?> updateMember(@RequestBody UpdateForm form) {
        try {
            Member updatedMember = userService.update(
                form.getCurrentUsername(), 
                form.getCurrentPassword(), 
                form.getNewUsername(), 
                form.getNewPassword()
            );
            // 변경된 아이디를 반환 (프론트엔드 갱신용)
            return ResponseEntity.ok(Map.of("username", updatedMember.getUsername()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("정보 수정 실패");
        }
    }

    // ... (기존 theme, withdraw, find-id, find-password, check-username 메서드 유지) ...
    @PostMapping("/theme")
    public ResponseEntity<String> updateTheme(@RequestBody Map<String, String> body) {
        boolean updated = userService.updateTheme(body.get("username"), body.get("theme"));
        return updated ? ResponseEntity.ok("테마 변경됨") : ResponseEntity.badRequest().body("실패");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody UserForm form) {
        boolean isDeleted = userService.delete(form.getUsername(), form.getPassword());
        return isDeleted ? ResponseEntity.ok("탈퇴 완료") : ResponseEntity.badRequest().body("비밀번호 불일치");
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

    // DTO 클래스들
    @Getter @Setter
    public static class UserForm {
        private String username;
        private String password;
        private String passwordConfirm;
        private String email;
    }

    // ★★★ [추가] 정보 수정용 DTO ★★★
    @Getter @Setter
    public static class UpdateForm {
        private String currentUsername;
        private String currentPassword;
        private String newUsername;
        private String newPassword;
    }
}