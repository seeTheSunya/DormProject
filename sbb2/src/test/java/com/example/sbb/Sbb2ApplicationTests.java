package com.example.sbb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest //스프링 부트 테스트 클래스로 지정
class Sbb2ApplicationTests {
	
	@Autowired //의존성 주입기능 하용하여 QuestionRepository의 객체 주입
	private QuestionRepository questionRepository;
	private AnswerRepository answerRepository;
/*
	@BeforeEach
	void beforeEach() {
        // 1. 외래 키 제약 조건 비활성화
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        
        // 2. 테이블 초기화 (다른 테이블도 있다면 함께 초기화)
        // jdbcTemplate.execute("TRUNCATE TABLE answer RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE question RESTART IDENTITY");
        
        // 3. 외래 키 제약 조건 다시 활성화
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
    */
	@Test //테스트 메서드로 지정
	void testJpa() {
		/*
		Question q1=new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1); //테이블 첫번째 행에 들어갈 데이터로 위 코드에 작성한 데이터들을 저낭
		
		Question q2=new Question();
		q2.setSubject("스프링 부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);
		List<Question> all=this.questionRepository.findAll(); //저장되어 있는 모든 데이터 조회
		assertEquals(2, all.size());  //매개변수=(기댓값, 실젯값)
		
		//Question q =all.get(0);
		//assertEquals("sbb가 무엇인가요?", q.getSubject());
		//Question q=this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		//assertEquals(1, q.getId());
		
		List<Question> qList=this.questionRepository.findBySubjectLike("sbb%");
		Question q=qList.get(0);
		
		assertEquals("sbb가 무엇인가요?", q.getSubject());
		*/
		Optional<Question> oq=this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q =oq.get();
		
		Answer a=new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);
	}
	
}
//[Run→RunAs→JUnitTest]순서대로선택하면  SbbApplicationTests클래스를실행
