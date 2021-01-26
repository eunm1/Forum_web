package com.eunm1.forum.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public LoginSuccessHandler(String defaultUrl){

        setDefaultTargetUrl(defaultUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
/*      방법1
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>
        alert('로그인 성공'); location.href='/forum'</script>");
        out.flush();*/

        request.getRequestDispatcher("/forum/login-ok").forward(request,response);
        System.out.println("성공");


        /*
        방법2
        HttpSession session = request.getSession();
        if(session != null){
            String redirectUrl = (String) session.getAttribute("prevPage");
            if(redirectUrl != null){
                //로그인전 URL로 이동하기
                session.removeAttribute("prevPage");
                getRedirectStrategy().sendRedirect(request,response,redirectUrl);
            }else{
                super.onAuthenticationSuccess(request,response,authentication);
            }
        }else{
            //기본url로 가도록함
            super.onAuthenticationSuccess(request,response,authentication);
        }*/
    }
}
