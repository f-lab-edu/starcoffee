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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/pay")
public class PayController {
    private final PayService payService;

    @Autowired
    public PayController(PayService payService) {
        this.payService = payService;
    }

    @GetMapping("/mycard")
    @ResponseStatus(HttpStatus.OK)
    public Card confirmMyCard(Card cardInfo) {
//        String memberId = SessionUtil.getMemberId(session);
//        Card cardInfo = logCardService.findCard(memberId);

        return cardInfo;
    }


    @PostMapping("/paying")
    @ResponseStatus(HttpStatus.OK)
    public PayResponse doPay(@RequestBody PayRequest payRequest, Card cardInfo) {
        PayResponse payResponse = payService.runPay(payRequest, cardInfo);

        return payResponse;
    }




}
