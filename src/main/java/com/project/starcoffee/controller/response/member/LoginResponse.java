package com.project.starcoffee.controller.response.member;

import com.project.starcoffee.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

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
    private UUID memberId;

    public static final LoginResponse success(UUID memberId) {
        return new LoginResponse(LoginStatus.SUCCESS, memberId);
    }

}
