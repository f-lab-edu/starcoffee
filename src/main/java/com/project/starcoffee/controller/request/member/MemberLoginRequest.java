package com.project.starcoffee.controller.request.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberLoginRequest {

    @NotNull
    private String loginId;

    @NotNull
    private String password;
}
