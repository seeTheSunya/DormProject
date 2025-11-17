package com.example.sbb.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final WebClient webClient;
    private final String apiKey;
    private final String model;
    
    private final String systemPrompt = 
        "당신은 '도란' 기숙사 커뮤니티를 위한 글쓰기 도우미입니다. " +
        "사용자가 요청한 주제에 대해 친절하고, 간결하며, 창의적인 텍스트를 생성합니다. " +
        "기숙사생에게 유용한 팁을 포함하는 것을 선호합니다. 모든 답변은 한국어로 합니다.";

    // 1. 생성자 수정:
    // WebClient.Builder를 주입받고, application.properties에서 키와 모델명을 읽어옵니다.
    public AiService(WebClient.Builder webClientBuilder,
                     @Value("${gemini.api.key}") String apiKey,
                     @Value("${gemini.api.model}") String model) {
        
        this.apiKey = apiKey;
        this.model = model;
        // WebClient 기본 URL 설정
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com").build();
    }

    // 2. generateText 메서드 수정:
    // WebClient를 사용하여 Gemini API를 직접 호출합니다.
    public String generateText(String userPrompt) {
        
        // API URL (모델명과 API 키 포함)
        String apiUrl = String.format("/v1beta/models/%s:generateContent?key=%s", this.model, this.apiKey);

        // Gemini API가 요구하는 JSON 페이로드(Payload) 생성
        Map<String, Object> systemPart = Map.of("text", systemPrompt);
        Map<String, Object> userPart = Map.of("text", userPrompt);
        Map<String, Object> systemInstruction = Map.of("parts", List.of(systemPart));
        Map<String, Object> contents = Map.of("parts", List.of(userPart));
        Map<String, Object> payload = Map.of(
            "contents", List.of(contents),
            "systemInstruction", systemInstruction
        );

        try {
            // WebClient로 API 호출 (block()을 사용해 동기식으로 결과를 기다림)
            GeminiResponse response = webClient.post()
                    .uri(apiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve() // 요청 실행
                    .bodyToMono(GeminiResponse.class) // 응답을 GeminiResponse 클래스로 변환
                    .block(); // 응답이 올 때까지 기다림

            // 응답 파싱
            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                return response.getCandidates().get(0).getContent().getParts().get(0).getText();
            }
            return "AI 응답 파싱에 실패했습니다.";
            
        } catch (Exception e) {
            System.err.println("Gemini API 호출 중 에러: " + e.getMessage());
            return "AI 서버 호출 중 에러가 발생했습니다. (API 키 확인 필요)";
        }
    }
}

// === JSON 응답을 받기 위한 DTO 클래스들 ===
// (파일 하단이나 별도 파일로 만들어도 됩니다)

class GeminiResponse {
    private List<Candidate> candidates;
    public List<Candidate> getCandidates() { return candidates; }
    public void setCandidates(List<Candidate> candidates) { this.candidates = candidates; }
}

class Candidate {
    private Content content;
    public Content getContent() { return content; }
    public void setContent(Content content) { this.content = content; }
}

class Content {
    private List<Part> parts;
    public List<Part> getParts() { return parts; }
    public void setParts(List<Part> parts) { this.parts = parts; }
}

class Part {
    private String text;
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}