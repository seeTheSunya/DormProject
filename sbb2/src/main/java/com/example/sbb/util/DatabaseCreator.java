package com.example.sbb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseCreator {
    public static void main(String[] args) {
        String dbName = "dorm";
        String url = System.getProperty("jdbc.url", "jdbc:mysql://localhost:3306?serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true");
        String username = System.getProperty("jdbc.user", "root");
        String password = System.getProperty("jdbc.password", "");
        
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + dbName + "` " +
                              "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            System.out.println("✓ Database '" + dbName + "' created/verified successfully!");
        } catch (Exception e) {
            System.err.println("Error creating database: " + e.getMessage());
            System.exit(1);
        }
    }
}

