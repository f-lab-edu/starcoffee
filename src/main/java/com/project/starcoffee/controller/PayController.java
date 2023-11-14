package com.project.starcoffee.controller;

import com.project.starcoffee.controller.request.pay.CancelRequest;
import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.pay.CancelResponse;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<LogCard> confirmMyCard(List<LogCard> cardList) {
        return cardList;
    }


    @PostMapping("/paying")
    @ResponseStatus(HttpStatus.OK)
    public PayResponse doPay(@RequestBody PayRequest payRequest) {
        PayResponse payResponse = payService.runPay(payRequest);
        return payResponse;
    }

    @PostMapping("/cancelling")
    @ResponseStatus(HttpStatus.OK)
    public CancelResponse doCancel(@RequestBody CancelRequest cancelRequest) {
        CancelResponse cancelResponse = payService.runCancel(cancelRequest);
        return cancelResponse;
    }

}
