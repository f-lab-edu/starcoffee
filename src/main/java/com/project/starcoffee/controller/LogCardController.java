package com.project.starcoffee.controller;

import com.project.starcoffee.aop.session.SessionMemberId;
import com.project.starcoffee.controller.request.card.CardNumberRequest;
import com.project.starcoffee.controller.request.pay.BalanceRequest;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.service.LogCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/logcard")
public class LogCardController {

    private final LogCardService logCardService;

    @Autowired
    public LogCardController(LogCardService logCardService) {
        this.logCardService = logCardService;
    }

    /**
     * 회원의 아이디로 카드등록을 한다.
     * @param cardNumberRequest 카드등록 정보
     * @param strMemberId aop -> 회원 아이디
     * @return
     */
    @PostMapping()
    @SessionMemberId
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Card> enrollCard(@RequestBody CardNumberRequest cardNumberRequest, String strMemberId) {
        Card card = logCardService.enrollCard(cardNumberRequest, strMemberId);

        return Optional.ofNullable(card)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * 회원의 카드들(List<LogCard>)을 조회한다.
     * @param strMemberId aop -> 회원 아이디
     * @return
     */
    @GetMapping()
    @SessionMemberId
    @ResponseStatus(HttpStatus.OK)
    public List<LogCard> findByMemberId(String strMemberId) {
        return logCardService.findByMemberId(strMemberId);
    }


    /**
     * 카드가 회원의 소지인지 조회한다.
     * @param memberId 회원 아이디
     * @param cardId 카드 아이디
     * @return
     */
    @GetMapping("/cardId")
    @ResponseStatus(HttpStatus.OK)
    public LogCard findByCardId(@RequestParam UUID memberId,
                                @RequestParam UUID cardId) {
        return logCardService.findByCardId(memberId, cardId);
    }


    /**
     * 회원카드의 잔액을 조회한다.
     * @param cardId 회원카드 아이디
     * @return
     */
    @GetMapping("/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int findByBalance(@PathVariable("id") UUID cardId) {
        return logCardService.findByBalance(cardId);
    }


    /**
     * 회원의 인출로 인해서 카드 정보를 업데이트 한다.
     * @param balanceRequest
     */
    @PatchMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    public Integer withDrawAmount(@RequestBody BalanceRequest balanceRequest) {
        Integer result = logCardService.withDrawAmount(balanceRequest);
        return result;
    }

}
