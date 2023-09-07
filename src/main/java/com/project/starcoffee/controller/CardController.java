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

    /**
     * 카드를 등록한다. (가맹점 시점)
     * @param cardRequest 카드 등록정보
     * @param session
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void enroll(@RequestBody @Valid CardRequest cardRequest, HttpSession session) {
//        String memberId = SessionUtil.getMemberId(session);
//        cardRequest.setLoginId(loginId);
        cardService.saveCard(cardRequest);
    }

    /**
     * 카드번호를 통해서 카드정보를 확인한다. (가맹점 시점)
     * @param cardNumber 카드번호
     * @return
     */
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


    /**
     * 카드 닉네임을 변경한다. (회원 시점)
     * @param cardInfo 변경할 카드정보(카드번호, 닉네임)
     */
    @PostMapping("/nickname")
    @ResponseStatus(HttpStatus.OK)
    public void updateNickName(@RequestBody @Valid CardNickNameRequest cardInfo) {
        cardService.updateNickName(cardInfo);
    }


    /**
     * 등록된 카드를 말소처리한다.
     * @param cardNumber 카드번호
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteCard(@RequestBody CardNumberRequest cardNumber) {
        cardService.deleteCard(cardNumber);
    }
}
