package com.project.starcoffee.controller.request.member;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequest {

    @NonNull
    private String loginId;

    @NonNull
    private String password;
}
