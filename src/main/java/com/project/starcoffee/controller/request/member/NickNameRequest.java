package com.project.starcoffee.controller.request.member;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class NickNameRequest {
    @NotNull
    @Size(min = 2, max = 10)
    private String afterNickname;

}
