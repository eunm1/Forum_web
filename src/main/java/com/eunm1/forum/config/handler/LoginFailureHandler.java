package com.eunm1.forum.config.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.cert.CertificateRevokedException;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        System.out.println("로그인 실패");

        //set error message 참고용, html표시는 보류
        String errorMsg = null;
        if(e instanceof BadCredentialsException || e instanceof InternalAuthenticationServiceException){
            errorMsg = "아이디나 비밀번호가 맞지 않습니다. 다시 확인해 주세요";
        }else if(e instanceof DisabledException){
            errorMsg = "계정이 비활성화 되었습니다. 다시 확인해 주세요";
        }else if(e instanceof CredentialsExpiredException){
            errorMsg = "비밀번호 유효기간이 만료 되었습니다. 관리자에게 문의하세요";
        }else{
            errorMsg = "알수없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요";
        }
        
        request.getRequestDispatcher("/forum/login-fail").forward(request,response);
    }
}
