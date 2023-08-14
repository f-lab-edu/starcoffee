package com.project.starcoffee.utils;

import javax.servlet.http.HttpSession;

public class SessionUtil {

    public static final String LOGIN_MEMBER = "loginMember";

    private SessionUtil() { }

    /**
     * 로그인한 고객의 아이디를 세션에서 꺼낸다.
     * @param session 사용자의 세션
     * @return
     */
    public static String getLoginMemberId(HttpSession session) {
        return (String) session.getAttribute(LOGIN_MEMBER);
    }

    /**
     * 로그인한 고객의 id를 세션에 저장한다.
     * @param session 사용자의 session
     * @param id 로그인한 고객의 id
     */
    public static void setLoginMemberId(HttpSession session, String id) {
        session.setAttribute(LOGIN_MEMBER, id);
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
