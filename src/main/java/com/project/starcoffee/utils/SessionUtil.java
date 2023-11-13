package com.project.starcoffee.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
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
        // Optional 활용하기
        // 온전한 값 리턴할 수 있도록 변경
        return (String) session.getAttribute(LOGIN_MEMBER);
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
