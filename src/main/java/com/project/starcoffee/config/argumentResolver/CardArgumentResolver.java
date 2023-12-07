package com.project.starcoffee.config.argumentResolver;

import com.project.starcoffee.domain.card.LogCard;
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
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CardArgumentResolver implements HandlerMethodArgumentResolver {

    private final LogCardService logCardService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LogCard.class) ||
                parameter.getParameterType().equals(List.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        HttpSession session = httpServletRequest.getSession();
        String memberId = SessionUtil.getMemberId(session);

        // 카드에 대한 유효성검사
        // Invalid
        List<LogCard> cardList = logCardService.findByMemberId(memberId);


        return cardList;
    }
}
