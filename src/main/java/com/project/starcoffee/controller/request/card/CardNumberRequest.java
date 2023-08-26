package com.project.starcoffee.controller.request.card;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
public class CardNumberRequest {

    @NotNull
    private String cardNumber;

}
