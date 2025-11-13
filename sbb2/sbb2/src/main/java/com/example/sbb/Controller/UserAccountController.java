package com.example.sbb.Controller;

import com.example.sbb.dto.SignupRequestDto;
import com.example.sbb.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
// ★★★ [추가] GET 요청을 처리하기 위해 GetMapping, RequestParam 임포트 ★★★
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor // final 필드를 자동으로 주입합니다 (EmailService)
public class UserAccountController {

    private final EmailService emailService;

    /**
     * @CrossOrigin(origins = "*")
     * 이것이 매우 중요합니다!
     * "Network Error" (CORS 오류)를 방지하기 위해
     * 모든 출처(http://... 로 열린 html 파일)에서의 요청을 허용합니다.
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/api/register") // (1) /api/register 라는 POST 주소를 만듭니다.
    public String registerUser(@RequestBody SignupRequestDto signupRequestDto) {
        // (2) Vue가 보낸 JSON 데이터를 DTO 객체로 받습니다.

        try {
            // (3) 임시 토큰 (실제로는 UUID 등으로 생성해야 함)
            String token = "temp-verification-token-12345"; 
            
            // (4) DTO에서 이메일 주소를 꺼내고, 생성한 토큰과 함께 EmailService로 전달
            emailService.sendEmail(signupRequestDto.getEmail(), token);

            // (5) 성공 메시지를 Vue.js로 반환
            return "회원가입 요청 성공. 이메일을 확인하세요.";

        } catch (Exception e) {
            // (6) 메일 발송 등에서 오류 발생 시
            e.printStackTrace();
            // (이 부분은 나중에 @RestControllerAdvice로 더 세련되게 처리할 수 있습니다)
            return "회원가입 요청 실패: " + e.getMessage();
        }
    }

    // ★★★ [추가된 메서드] ★★★
    // 이메일 인증 링크(GET /verify?token=...)를 처리할 엔드포인트
    @CrossOrigin(origins = "*") // CORS 오류 방지
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token) {
        // @RequestParam("token"): URL의 ?token=... 값을 문자열 token 변수에 담아줍니다.

        // (1) 실제로는 DB에서 이 토큰을 가진 사용자가 있는지 확인해야 합니다.
        // (2) 토큰이 유효하다면, 해당 사용자의 '인증' 상태를 true로 변경(UPDATE)해야 합니다.
        
        // (3) 지금은 임시로 토큰 값만 확인합니다.
        if (token.equals("temp-verification-token-12345")) {
            // (4) 인증 성공 페이지 (간단한 HTML 응답)
            return "<h1>이메일 인증이 완료되었습니다.</h1><p>이제 로그인할 수 있습니다.</p>";
        } else {
            // (5) 인증 실패 페이지
            return "<h1>오류</h1><p>유효하지 않은 토KDS입니다.</p>";
        }
    }
}