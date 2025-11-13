package com.example.sbb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
    private final JavaMailSender mailSender;
    private final String fromEmail="jjs23574@chungbuk.ac.kr"; //보내는사람 메일=관리자메일

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail, String token) {
        String link = "http://localhost:8080/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("회원가입 이메일 인증");
        message.setText("안녕하세요!\n아래 링크를 클릭하여 회원가입을 완료하세요:\n" + link);
        message.setFrom(fromEmail);
        mailSender.send(message);
    }
}
