package com.project.starcoffee.controller.argument;

import com.project.starcoffee.controller.request.member.MemberLoginRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginIdPasswordArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(IdPass.class) != null
                && parameter.getParameterType().equals(MemberLoginRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String loginId = request.getParameter("loginId");
        String password = request.getParameter("password");

        MemberLoginRequest loginRequest = new MemberLoginRequest();
        loginRequest.setLoginId(loginId);
        loginRequest.setPassword(password);

        return loginRequest;
    }
}
