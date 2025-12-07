package com.example.sbb; // ★ 패키지명 수정됨

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // HTML 파일(프론트)이 스프링 부트(백엔드) 데이터를 가져갈 수 있게 허용
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 모든 주소에서 접속 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
    
    // ★ 개발 모드: 정적 리소스 캐싱 비활성화 (새로고침 시 바로 반영)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0); // 캐시 비활성화
    }
}