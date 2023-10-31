package com.project.starcoffee.config;

import com.project.starcoffee.config.login.LoginRequired;
import com.project.starcoffee.controller.response.ResponseMessage;
import com.project.starcoffee.utils.SessionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SessionHandler extends RequestMappingHandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            return hm.hasMethodAnnotation(LoginRequired.class);
        }
        return false;
    }

    @Override
    public Mono<HandlerResult> handle(ServerWebExchange exchange, Object handler) {
        HandlerMethod hm = (HandlerMethod) handler;
        return exchange.getSession().flatMap(session -> {
            if (!session.getAttributes().containsKey(SessionUtil.LOGIN_MEMBER)) {
                Object response = ResponseEntity.status(440).body(new ResponseMessage("로그인하지 않았거나, 세션이 만료되었습니다."));
                return Mono.just(new HandlerResult(handler, response, hm.getReturnType()));
            }
            return super.handle(exchange, handler);
        });
    }



}
