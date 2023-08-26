package com.project.starcoffee.controller.request.member;

import com.project.starcoffee.validation.email.ValidEmail;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@ValidEmail
public class EmailRequest {
    @NotNull
    private String afterEmail;

}
