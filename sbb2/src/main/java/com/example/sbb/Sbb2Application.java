package com.example.sbb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

// ★ [수정] Vertex AI import 문 삭제
// import org.springframework.ai.model.vertexai.autoconfigure.gemini.VertexAiGeminiChatAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Sbb2Application {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Sbb2Application.class);
		
		// 데이터베이스 초기화 리스너 추가 (가장 먼저 실행)
		app.addListeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
			String dbName = "dorm";
			String username = event.getEnvironment().getProperty("spring.datasource.username", "root");
			String password = event.getEnvironment().getProperty("spring.datasource.password", "");
			
			String urlWithoutDb = "jdbc:mysql://localhost:3306?serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true";
			
			try (java.sql.Connection conn = java.sql.DriverManager.getConnection(urlWithoutDb, username, password);
				 java.sql.Statement stmt = conn.createStatement()) {
				
				stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + dbName + "` " +
								  "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
				System.out.println("✓ Database '" + dbName + "' checked/created successfully!");
			} catch (Exception e) {
				System.err.println("⚠ Database initialization error: " + e.getMessage());
				e.printStackTrace();
			}
		});
		
		app.run(args);
	}

}
