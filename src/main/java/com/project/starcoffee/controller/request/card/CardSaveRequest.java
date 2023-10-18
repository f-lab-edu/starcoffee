package com.project.starcoffee.controller.request.card;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CardSaveRequest {

    @NotNull
    private String cardNumber;

    @NotNull
    private String pinNumber;

}
