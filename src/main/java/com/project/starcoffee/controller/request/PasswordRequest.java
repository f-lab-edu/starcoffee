package com.project.starcoffee.controller.request;

import com.project.starcoffee.validation.password.ValidPassword;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Getter
@ValidPassword
public class PasswordRequest {

    @NotNull
    private String beforePassword;

    @NotNull
    private String afterPassword;
}
