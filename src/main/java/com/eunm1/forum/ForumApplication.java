package com.eunm1.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@EnableJpaAuditing //JPA Auditing 기능 활성화
@SpringBootApplication
public class ForumApplication {

	public static void main(String[] args) {

		SpringApplication.run(ForumApplication.class, args);
	}

	//html에서 (add_forum) input에 숨겨진 _method의 값을 필터링한다.
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
}
/*
JPA Auditing(감시하는 것)이란?
Spring Data JPA에서 시간에 대해서 자동으로 값을 넣어주는 기능
@MappedSuperclass : JPA Entity클래스들이 해당 추상클래스를 상속할 경우 createDat, modifiedDate를 컬럼으로 인삭
@EntityListeners(AuditingEntityListener.class) : 해당 클래스에서 Auditing기능을 포함
@CreatedDate : Entity가 생성되어 저장될때 시간이 자동저장
@LastModifiedDate : 조회한 Entity의 값을 변경할 때 시간이 자동 저장
*/