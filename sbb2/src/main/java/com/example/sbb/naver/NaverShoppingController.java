package com.example.sbb.naver;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/naver")
@RestController
public class NaverShoppingController {

    private final NaverShoppingService naverShoppingService;

    @GetMapping("/search")
    public NaverShopDto search(@RequestParam("query") String query) {
        return naverShoppingService.search(query);
    }
}