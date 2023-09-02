package com.project.starcoffee.controller.response.card;

import com.project.starcoffee.domain.card.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CardInfoResponse {

    enum RequestStatus {
        SUCCESS, FAIL, DELETED
    }

    public static final CardInfoResponse FAIL = new CardInfoResponse(RequestStatus.FAIL, null);

    @NotNull
    private RequestStatus status;
    private Card cardInfo;


    public static final CardInfoResponse success(Card cardInfo) {
        return new CardInfoResponse(RequestStatus.SUCCESS, cardInfo);
    }
}
