package com.eunm1.forum.config.AuthProvider;

import com.eunm1.forum.config.MyMemberDetail;
import com.eunm1.forum.service.MemberService;
import com.eunm1.forum.util.RSAUtil;
import com.eunm1.forum.util.SessionUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RSAUtil rsaUtil = new RSAUtil();

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String loginId = authentication.getName();
        String loginPw = (String) authentication.getCredentials();

        PrivateKey privateKey = (PrivateKey)SessionUtil.getAttribute("PK");

//        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
//        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Form에서 전달 된, name 태그 설정이 username-parameter, password-parameter로 되있는 값을 읽어온다

        //System.out.println(loginId);
        //System.out.println(loginPw);
        //System.out.println(privateKey);
        if(privateKey == null){
            throw new BadCredentialsException("비정상적인 접근");
        }else{
            loginId = rsaUtil.getDecryptText(privateKey, loginId);
            loginPw = rsaUtil.getDecryptText(privateKey, loginPw);
        }

        UsernamePasswordAuthenticationToken authenticationToken;

         MyMemberDetail userDetails =(MyMemberDetail) memberService.loadUserByUsername(loginId);

        if(!passwordEncoder.matches(loginPw,userDetails.getPassword())){
            throw new BadCredentialsException("Password 불일치");
        }else{

            authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginId,userDetails.getPassword(),userDetails.getAuthorities());
            authenticationToken.setDetails(userDetails);
        }

        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
