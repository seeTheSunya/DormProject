package com.example.sbb;

import java.util.List;

//데이터 관리 좀터 편리
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;

//import jakarta.transaction.Transactional;

public interface QuestionRepository extends JpaRepository<Question, Integer>{
	Question findBySubject(String subject);
	/*
	@Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE question RESTART IDENTITY", nativeQuery = true)
    void truncateTable();
	*/
	Question findBySubjectAndContent(String subject, String content);
	List<Question> findBySubjectLike(String subject);
}
