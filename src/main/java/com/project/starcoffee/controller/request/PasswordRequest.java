package com.project.starcoffee.controller.request;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class PasswordRequest {

    @NonNull
    private String beforePassword;

    @NonNull
    private String afterPassword;
}
