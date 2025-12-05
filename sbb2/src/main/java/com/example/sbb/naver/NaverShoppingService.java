package com.example.sbb.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Service
public class NaverShoppingService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    @Value("${naver.url.search.shop}")
    private String naverShopUrl;

    public NaverShopDto search(String query) {
        // 1. 검색어 인코딩 및 URL 생성
        URI uri = UriComponentsBuilder
                .fromUriString(naverShopUrl)
                .queryParam("query", query)
                .queryParam("display", 5)  // 5개만 검색
                .queryParam("start", 1)
                .queryParam("sort", "sim") // 정확도순 (sim) 또는 최저가순 (asc)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        // 2. 헤더에 Client ID와 Secret 추가
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .build();

        // 3. 요청 전송 및 응답 받기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NaverShopDto> response = restTemplate.exchange(req, NaverShopDto.class);

        return response.getBody();
    }
}