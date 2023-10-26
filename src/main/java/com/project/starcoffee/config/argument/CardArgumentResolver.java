package com.project.starcoffee.config.argument;

import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.service.LogCardService;
import com.project.starcoffee.utils.SessionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CardArgumentResolver implements HandlerMethodArgumentResolver {

    private final LogCardService logCardService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Card.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        HttpSession session = httpServletRequest.getSession();
        String memberId = SessionUtil.getMemberId(session);

        Optional<Card> cardInfo = logCardService.findCard(memberId);

        cardInfo.ifPresentOrElse(card -> {},
                ()-> { throw new RuntimeException("카드를 찾을 수 없습니다."); }
        );

        return cardInfo;
    }
}
