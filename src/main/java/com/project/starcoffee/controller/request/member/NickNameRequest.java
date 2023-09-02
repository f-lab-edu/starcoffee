package com.project.starcoffee.controller.request.member;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class NickNameRequest {
    @NotNull
    private String afterNickname;

}
