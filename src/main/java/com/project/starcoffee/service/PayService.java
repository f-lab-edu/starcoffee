package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.exception.BalanceException;
import com.project.starcoffee.repository.LogCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class PayService {
    private final OrderService orderService;
    private final LogCardRepository logCardRepository;

    @Autowired
    public PayService(OrderService orderService, LogCardRepository logCardRepository) {
        this.orderService = orderService;
        this.logCardRepository = logCardRepository;
    }

    @Transactional
    public PayResponse runPay(PayRequest payRequest, Card cardInfo) {
        int orderPrice = payRequest.getFinalPrice();
        int cardAmount = cardInfo.getCardAmount();
        UUID cardId = cardInfo.getCardId();

        if (cardAmount >= orderPrice) {
            int balance = cardAmount - orderPrice;
            int result = logCardRepository.updateAmount(cardId, balance);

            if (result != 1) {
                log.info("정상적으로 금액이 처리 되지 못했습니다.");
                throw new RuntimeException("데이터베이스에 잔액이 업데이트되지 못했습니다.");
            }
        } else {
            log.info("잔액이 부족합니다.");
            throw new BalanceException("잔액이 부족합니다.");
        }



        return PayResponse.builder()
                .memberId(payRequest.getMemberId())
                .storeId(payRequest.getStoreId())
                .price(payRequest.getFinalPrice())
                .build();
    }

}
