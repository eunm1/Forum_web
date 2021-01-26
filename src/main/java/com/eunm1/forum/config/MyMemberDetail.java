package com.eunm1.forum.config;

import com.eunm1.forum.db.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class MyMemberDetail implements UserDetails {
    private Long Id;
    private String loginId;
    private String loginPw;
    private String nickname;
    private String auth;

    public MyMemberDetail(Member member){
        this.Id = member.getId();
        this.loginId = member.getLoginId();
        this.loginPw = member.getLoginPw();
        this.nickname = member.getNickname();
        this.auth = "ROLE_" + member.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.auth));
    }

    @Override
    public String getPassword() {
        return this.loginPw;
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId(){return this.Id;}
}
