package com.project.starcoffee.config.aop;

import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Slf4j
@Aspect
@Component
public class HttpSessionAspect {

    @Around("@annotation(com.project.starcoffee.config.aop.SessionMemberId)")
    public Object injectHttpSession(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes()))
                .getRequest().getSession();
        String memberId = SessionUtil.getMemberId(session);

        // SessionUtil 로직 안으로 넣기 ! (null 체크)
        if (memberId.isEmpty()) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "NO_LOGIN") { };
        }

        Object[] args = joinPoint.getArgs();

        // 시간복잡도, args.length 를 가지고 args 를 가지고 오기
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                args[i] = memberId;
            }
        }

        Object result = joinPoint.proceed(args);

        return result;
    }
}
