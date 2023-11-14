package com.project.starcoffee.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SessionUtil {

    public static final String LOGIN_MEMBER = "loginMember";

    private SessionUtil() { }

    /**
     * 로그인한 고객의 아이디를 세션에서 꺼낸다.
     * @param session 사용자의 세션
     * @return
     */
    public static String getMemberId(HttpSession session) {
        Optional<String> optionalMemberId = Optional.ofNullable((String) session.getAttribute(LOGIN_MEMBER));
        optionalMemberId.orElseThrow(() ->
                new RuntimeException("고객의 아이디 세션을 찾을 수 없습니다."));

        return optionalMemberId.get();
    }

    /**
     * 로그인한 고객의 id를 세션에 저장한다.
     * @param session 사용자의 session
     * @param id 로그인한 고객의 id
     */
    public static void setMemberId(HttpSession session, UUID id) {
        String memberIdStr = id.toString();
        session.setAttribute(LOGIN_MEMBER, memberIdStr);
    }

    /**
     * 고객의 로그인 정보를 삭제한다.
     * @param session 사용자의 세션
     */
    public static void logoutMember(HttpSession session) {
        session.removeAttribute(LOGIN_MEMBER);

    }

    /**
     * 해당 세션의 정보를 모두 삭제한다.
     * @param session
     */
    public static void clear(HttpSession session) {
        session.invalidate();
    }

}
