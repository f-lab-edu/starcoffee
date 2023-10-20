package com.project.starcoffee.controller;

import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.service.LogCardService;
import com.project.starcoffee.service.PayService;
import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/pay")
public class PayController {

    private final PayService payService;
    private final LogCardService logCardService;

    @Autowired
    public PayController(PayService payService, LogCardService logCardService) {
        this.payService = payService;
        this.logCardService = logCardService;
    }

    @GetMapping("/mycard")
    @ResponseStatus(HttpStatus.OK)
    public Card confirmMyCard(HttpSession session) {
        String memberId = SessionUtil.getMemberId(session);
        Card cardInfo = logCardService.findCard(memberId);

        return cardInfo;
    }


    @PostMapping("/paying")
    @ResponseStatus(HttpStatus.OK)
    public PayResponse doPay(@RequestBody PayRequest payRequest, HttpSession session) {
        Card cardInfo = confirmMyCard(session);
        PayResponse payResponse = payService.runPay(payRequest, cardInfo);

        return payResponse;
    }




}
