package com.project.starcoffee.controller;

import com.project.starcoffee.controller.request.card.CardNickNameRequest;
import com.project.starcoffee.controller.request.card.CardNumberRequest;
import com.project.starcoffee.controller.request.card.CardRequest;
import com.project.starcoffee.controller.response.card.CardInfoResponse;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.service.CardService;
import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void enroll(@RequestBody @Valid CardRequest cardRequest, HttpSession session) {
        String loginId = SessionUtil.getLoginId(session);
        cardRequest.setLoginId(loginId);
        cardService.saveCard(cardRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CardInfoResponse> findById(@RequestBody CardNumberRequest cardNumber) {
        Optional<Card> cardInfo = cardService.findById(cardNumber.getCardNumber());

        ResponseEntity<CardInfoResponse> responseEntity = cardInfo
                .map(card -> {
                    CardInfoResponse cardInfoResponse = CardInfoResponse.success(card);
                    return new ResponseEntity<>(cardInfoResponse, HttpStatus.OK);
                }).orElseThrow(() -> new RuntimeException("카드 정보를 찾을 수 없습니다."));
        return responseEntity;
    }


    @PostMapping("/nickname")
    @ResponseStatus(HttpStatus.OK)
    public void updateNickName(@RequestBody @Valid CardNickNameRequest cardInfo) {
        cardService.updateNickName(cardInfo);
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteCard(@RequestBody CardNumberRequest cardNumber) {
        cardService.deleteCard(cardNumber);
    }
}
