package com.project.starcoffee.controller.argument;

import com.project.starcoffee.controller.request.member.MemberLoginRequest;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.repository.MemberRepository;
import com.project.starcoffee.utils.SHA256Util;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginProcessArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginProcess.class)
                && parameter.getParameterType().equals(MemberLoginRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String loginId = request.getParameter("loginId");
        String password = request.getParameter("password");

        String cryptoPassword = SHA256Util.encryptSHA256(password);
        Member member = memberRepository.findByIdAndPassword(loginId, cryptoPassword);

        if (member == null) {
            log.error("not found Member ERROR! {}", member);
            throw new RuntimeException("not found Member ERROR! 회원을 찾을 수 없습니다.");
        }

        return member;

    }

}
