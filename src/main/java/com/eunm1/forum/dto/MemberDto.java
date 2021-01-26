package com.eunm1.forum.dto;

import com.eunm1.forum.db.entity.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class MemberDto {
    private Long id; //article에서 외부참조하는 값
    private String loginId;
    private String loginPw;
    private String nickname;
    private LocalDateTime createdDate;
    private String role;

    public Member toEntity(){
        Member member_build = Member.builder()
                .id(id)
                .loginId(loginId)
                .loginPw(loginPw)
                .nickname(nickname)
                .role(role)
                .build();
        return member_build;
    }

    @Builder // 객체에 builder를 이용한다.
    public MemberDto(Long id, String loginId, String loginPw, String nickname, LocalDateTime createdDate, String role){
        this.id = id;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.nickname = nickname;
        this.createdDate = createdDate;
        this.role = role;
    }
}
