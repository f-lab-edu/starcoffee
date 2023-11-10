package com.project.starcoffee.config.aop;

import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class HttpSessionAspect {

    @Around("@annotation(com.project.starcoffee.config.aop.SessionMemberId)")
    public Object injectHttpSession(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes()))
                .getRequest().getSession();
        String memberId = SessionUtil.getMemberId(session);

        if (memberId.isEmpty()) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "NO_LOGIN") { };
        }

        log.info("MemberId : {}", memberId);
        Object[] args = joinPoint.getArgs();
        log.info("Method arguments: {}", Arrays.toString(args));

        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                args[i] = memberId;
            }
        }

        Object result = joinPoint.proceed(args);

        return result;
    }
}
