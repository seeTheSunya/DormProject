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
        // 1. 비밀번호 일치 여부 확인
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        // 2. ★ 추가: 아이디 중복 확인 (DB 에러 방지)
        if (userService.isUsernameDuplicate(form.getUsername())) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }

        // 3. ★ 추가: 학교 이메일 도메인 검증 (.ac.kr로 끝나는지 확인)
        if (!form.getEmail().endsWith(".ac.kr")) {
            return ResponseEntity.badRequest().body("학교 이메일(@*.ac.kr)만 사용 가능합니다.");
        }

        // 4. ★ 추가: 이메일 중복 확인 (이상한 에러 메시지 방지)
        // (기존의 findUsername 메서드는 이메일로 아이디를 찾는 기능이므로, 결과가 null이 아니면 이미 이메일이 존재한다는 뜻입니다.)
        if (userService.findUsername(form.getEmail()) != null) {
            return ResponseEntity.badRequest().body("이미 등록된 이메일입니다.");
        }

        try {
            // 회원가입: 이메일만 발송하고 DB에는 임시 저장 (인증 성공 시에만 실제 회원이 됨)
            userService.createAndSendEmail(form.getUsername(), form.getPassword(), form.getEmail());
            return ResponseEntity.ok("이메일을 발송했습니다. 이메일 인증을 완료하면 회원가입이 완료됩니다.");
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            // 이메일 인증 실패인 경우 더 명확한 메시지
            if (errorMsg != null && (errorMsg.contains("Authentication failed") || errorMsg.contains("Mail"))) {
                return ResponseEntity.badRequest().body("이메일 발송 설정 오류입니다. 관리자에게 문의하세요. (오류: " + errorMsg + ")");
            }
            return ResponseEntity.badRequest().body("회원가입 실패: " + errorMsg);
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
            // ★ 수정: 로그인 성공 시 사용자 이름과 저장된 테마 정보를 함께 반환
            return ResponseEntity.ok(Map.of(
                "username", member.getUsername(),
                "theme", member.getTheme() != null ? member.getTheme() : "light" // 테마가 없으면 기본값 light
            ));
        } else {
            return ResponseEntity.status(401).body("아이디/비번이 틀렸거나, 이메일 인증이 완료되지 않았습니다.");
        }
    }

    // ★ 추가: 테마 변경 API
    @PostMapping("/theme")
    public ResponseEntity<String> updateTheme(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String theme = body.get("theme"); // "light", "dark", "blue", "pink" 등
        
        boolean updated = userService.updateTheme(username, theme);
        if (updated) {
            return ResponseEntity.ok("테마가 변경되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("테마 변경 실패");
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
    
    // ★ 회원 탈퇴 API
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody UserForm form) {
        boolean isDeleted = userService.delete(form.getUsername(), form.getPassword());
        if (isDeleted) {
            return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }
    }

    @Getter @Setter
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
        private String currentPassword; 
        private String newPassword;    
    }
}