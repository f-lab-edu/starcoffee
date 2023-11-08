package com.project.starcoffee.controller;

import com.project.starcoffee.controller.request.card.CardNickNameRequest;
import com.project.starcoffee.controller.request.card.CardNumberRequest;
import com.project.starcoffee.controller.request.card.CardRequest;
import com.project.starcoffee.controller.response.card.CardInfoResponse;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * 카드를 등록한다. (카드본점 시점)
     * @param cardRequest 카드 등록정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void enroll(@RequestBody @Valid CardRequest cardRequest) {
        cardService.saveCard(cardRequest);
    }

    /**
     * 카드번호를 통해서 카드정보를 확인한다. (카드본점 시점)
     * @param cardNumberRequest 카드번호
     * @return
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CardInfoResponse> findByCardNumber(@RequestBody CardNumberRequest cardNumberRequest) {
        Card cardInfo = cardService.findByCardNumber(cardNumberRequest.getCardNumber());
        CardInfoResponse cardInfoResponse = CardInfoResponse.success(cardInfo);
        return new ResponseEntity<>(cardInfoResponse, HttpStatus.OK);
    }

    /**
     * 등록된 카드를 말소처리한다. (카드본점 시점)
     * @param cardNumber 카드번호
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteCard(@RequestBody CardNumberRequest cardNumber) {
        cardService.deleteCard(cardNumber);
    }


    /**
     * 카드번호로 카드를 조회한다.
     * @param cardNumber 카드번호
     * @return
     */
    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public Card findCardByNumber(String cardNumber) {
        return cardService.findByCardNumber(cardNumber);
    }

    /**
     * 등록된 카드 닉네임을 변경한다.
     * @param cardInfo 변경할 카드정보(카드번호, 닉네임)
     */
    @PostMapping("/card/nickname")
    @ResponseStatus(HttpStatus.OK)
    public void updateNickName(@RequestBody @Valid CardNickNameRequest cardInfo) {
        cardService.updateNickName(cardInfo);
    }











}
