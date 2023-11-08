package com.project.starcoffee.controller.request.member;

import com.project.starcoffee.validation.phone.ValidPhone;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@ValidPhone
public class PhoneRequest {
    @NotNull
    private String afterPhoneNumber;
}
