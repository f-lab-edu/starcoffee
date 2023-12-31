package com.project.starcoffee.aop.session;

import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class HttpSessionAspectImp {

    @Around("@annotation(com.project.starcoffee.aop.session.SessionMemberId)")
    public Object injectHttpSession(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes()))
                .getRequest().getSession();
        String memberId = SessionUtil.getMemberId(session);

        Object[] args = joinPoint.getArgs();

        Arrays.setAll(args, i -> args[i] != null ? args[i] : memberId);

        Object result = joinPoint.proceed(args);

        return result;
    }
}
