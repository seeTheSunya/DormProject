package com.example.sbb.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
    private final JavaMailSender mailSender;
    // application.properties에 설정된 username을 가져와서 쓰는 것이 좋지만, 
    // 일단 보내주신대로 하드코딩된 이메일을 사용합니다.
    private final String fromEmail = "jjs23574@chungbuk.ac.kr"; 

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 사용자님의 코드 반영: 토큰을 받아 링크를 만들고 메일을 보냅니다.
    public void sendEmail(String toEmail, String token) {
        // ★ 주의: UserRestController의 경로가 /api/verify 이므로 주소를 수정했습니다.
        String link = "http://localhost:8081/api/verify?token=" + token;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("회원가입 이메일 인증");
        message.setText("안녕하세요!\n아래 링크를 클릭하여 회원가입을 완료하세요:\n" + link);
        message.setFrom(fromEmail);
        
        mailSender.send(message);
    }
}