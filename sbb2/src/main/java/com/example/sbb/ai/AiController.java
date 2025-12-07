package com.example.sbb.ai;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RequestMapping("/api/ai")
@RequiredArgsConstructor
@RestController
public class AiController {

    // ★ 수정: final 키워드 추가 (이게 없어서 주입이 안 되고 null이었던 것입니다!)
    private final AiService aiService;

    // 프론트엔드가 호출할 API 엔드포인트
    @PostMapping("/generate")
    public Map<String, String> generateText(@RequestBody PromptRequest request) {
        String generatedText = aiService.generateText(request.getPrompt());
        return Map.of("text", generatedText);
    }

    @Getter @Setter
    static class PromptRequest {
        private String prompt;
    }
}