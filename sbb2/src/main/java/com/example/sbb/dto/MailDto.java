package com.example.sbb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MailDto {
    private String address; // 받는 사람 이메일
    private String title;   // 제목
    private String message; // 내용
    
}