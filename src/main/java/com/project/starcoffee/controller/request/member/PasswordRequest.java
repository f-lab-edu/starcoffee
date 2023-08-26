package com.project.starcoffee.controller.request.member;

import com.project.starcoffee.validation.password.ValidPassword;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@ValidPassword
public class PasswordRequest {

    @NotNull
    private String beforePassword;

    @NotNull
    private String afterPassword;
}
