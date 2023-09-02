package com.project.starcoffee.controller.request.card;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class CardNickNameRequest {

    @NotNull
    private String cardNumber;

    @NotNull
    @Size(min=2, max=10, message = "카드별칭은 2~10자리까지 입력가능합니다.")
    private String cardNickName;
}
