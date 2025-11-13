package com.example.sbb.dto;

import lombok.Getter;
import lombok.Setter;

// Vue.js가 보낸 JSON 데이터를 담기 위한 객체입니다.
// (롬복(Lombok)이 설치되어 있어야 @Getter, @Setter가 작동합니다)
@Getter
@Setter
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;
}