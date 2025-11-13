package com.example.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //이 클래스를 스프링 부트의 컨트롤러로 지정
public class MainController {
	@GetMapping("/test")
    public String test() {
        return "site"; // templates/site.html 을 찾음
	}
}
