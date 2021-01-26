package com.eunm1.forum.config;

import com.eunm1.forum.config.AuthProvider.CustomAuthenticationProvider;
import com.eunm1.forum.config.handler.LoginFailureHandler;
import com.eunm1.forum.config.handler.LoginSuccessHandler;
import com.eunm1.forum.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@EnableWebSecurity //spring security를 적용한다는 어노테이션
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private final MemberService memberService;



    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/forum/main","/forum/detail_article/{id}").permitAll()
                .antMatchers("/forum/signup").anonymous()
                .antMatchers("/forum/add_article").hasRole("USER")
                .and()
                .csrf().disable()
             .formLogin()
                .loginPage("/login")
                //.successHandler(successHandler())
                .failureHandler(failureHandler())
                .defaultSuccessUrl("/forum/login-ok")
                .failureForwardUrl("/forum/login-fail")
                .permitAll()
                .usernameParameter("loginId") //loadUserByUsername의 파라미터 값(html의 input-name에 해당하는 값)
                .passwordParameter("loginPw")
                .and()
             .logout()
                .logoutSuccessUrl("/forum/logout")
                .invalidateHttpSession(true);
    }//글쓰기, 수정, 삭제할때 USER가 없으면 허가안되게하기

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        return new CustomAuthenticationProvider();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler(){
        return new LoginSuccessHandler("/forum");
    }

    @Bean
    public AuthenticationFailureHandler failureHandler(){
        return new LoginFailureHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
