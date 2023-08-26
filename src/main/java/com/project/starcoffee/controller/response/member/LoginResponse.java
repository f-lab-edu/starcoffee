package com.project.starcoffee.controller.response.member;

import com.project.starcoffee.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginResponse {

    public static final LoginResponse FAIL = new LoginResponse(LoginStatus.FAIL);

    enum LoginStatus {
        SUCCESS, FAIL, DELETED
    }

    @NonNull
    private LoginStatus status;
    private Member memberInfo;

    public static final LoginResponse success(Member memberInfo) {
        return new LoginResponse(LoginStatus.SUCCESS, memberInfo);
    }

}
