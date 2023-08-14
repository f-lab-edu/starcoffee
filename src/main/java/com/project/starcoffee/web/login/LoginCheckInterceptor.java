package com.project.starcoffee.web.login;

import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        log.info("로그인 인증체크 인터셉터 실행 => {}", requestURI);
        HttpSession session = request.getSession();

        if (session == null || session.getAttribute(SessionUtil.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            return false;
        }

        log.info("로그인 인증체크 인터셉터 완료");
        return true;
    }
}
