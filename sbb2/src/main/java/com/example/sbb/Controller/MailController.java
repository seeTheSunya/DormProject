package com.example.sbb.Controller;

// 1. Map을 사용하기 위해 import 추가
import java.util.Map;

import com.example.sbb.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;

    @PostMapping("/mail/send")
    // 2. @RequestBody MailDto -> @RequestBody Map<String, String>으로 변경
    public String sendMail(@RequestBody Map<String, String> payload) {
        
        // 3. Map에서 "toEmail"과 "token" 값을 꺼냅니다.
        String toEmail = payload.get("toEmail");
        String token = payload.get("token");

        // 4. 서비스의 2개 파라미터 메서드 호출
        emailService.sendEmail(toEmail, token);
        
        return "메일 발송 완료";
    }
}