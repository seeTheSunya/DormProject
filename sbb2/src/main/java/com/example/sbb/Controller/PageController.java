package com.example.sbb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String communityPage() {
        // static 폴더에 있는 site.html을 열도록 설정
    	return "redirect:/site.html";
    }
}
