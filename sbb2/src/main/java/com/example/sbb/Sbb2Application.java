package com.example.sbb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ★ [수정] Vertex AI import 문 삭제
// import org.springframework.ai.model.vertexai.autoconfigure.gemini.VertexAiGeminiChatAutoConfiguration;

// ★ [수정] @SpringBootApplication에서 exclude 삭제
@SpringBootApplication
public class Sbb2Application {

	public static void main(String[] args) {
		SpringApplication.run(Sbb2Application.class, args);
	}

}