package com.eunm1.forum.db.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String loginId;

    @Column(length = 100, nullable = false)
    private String loginPw;

    @Column(length = 100, nullable = false)
    private String nickname; //member클래스의 id를 외부참조해야한다.

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Column(length = 50)
    private String role;

    @Builder // 객체에 builder를 이용한다.
    public Member(Long id, String loginId, String loginPw, String nickname, String role){
        this.id = id;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.nickname = nickname;
        this.role = role;
    }
}
