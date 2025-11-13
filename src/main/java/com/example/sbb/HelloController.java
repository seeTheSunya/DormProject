package com.example.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //이 글래스가 스프링 부트의 컨트롤러 기능 수행
public class HelloController {
	@GetMapping("/hello") //http://localhost:8080/hello URL요청 발생->hello메서드 실행(매핑)
	@ResponseBody
	public String hello() {
		return "Hello ";
	}
	/*
	@GetMapping("/")
    public String root() {
        return "redirect:/question/list";
    }
    */

}
