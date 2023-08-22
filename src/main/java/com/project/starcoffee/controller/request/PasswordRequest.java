package com.project.starcoffee.controller.request;

import com.project.starcoffee.validation.password.ValidPassword;
import lombok.Getter;
import lombok.NonNull;

@Getter
@ValidPassword
public class PasswordRequest {

    @NonNull
    private String beforePassword;

    @NonNull
    private String afterPassword;
}
