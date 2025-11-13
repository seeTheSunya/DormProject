package com.example.sbb;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity //스프링 부트가 엔티티로 인식함
public class Answer {

	@Id //id속성을 기본키로 저장. 중복 id없게 하는 기능
	@GeneratedValue(strategy=GenerationType.IDENTITY) //데이터저장하면 자동으로 1씩 증가. strategy=GenerationType.IDENTITY는고유한번호를생성하는방법을지정하는부분. GenerationType.IDENTITY는해당속성만별도로번호가차례대로늘어나도록할때사용함
	private Integer id;
	
	@Column(columnDefinition="TEXT") //텍스트를 열 데이터로 넣을 수 있게
	private String content;
	
	@CreatedDate
	private LocalDateTime createDate;
	
	@ManyToOne    //하나의 질문에 답변은 여러개
	private Question question;
}

