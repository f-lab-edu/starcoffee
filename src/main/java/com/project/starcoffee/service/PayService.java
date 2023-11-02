package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.card.LogCard;
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
    private final LogCardRepository logCardRepository;

    @Autowired
    public PayService(LogCardRepository logCardRepository) {
        this.logCardRepository = logCardRepository;
    }

    // 비동기
    @Transactional
    public PayResponse runPay(PayRequest payRequest) {
        int cardAmount = payRequest.getFinalPrice();    // 결제 금액
        int cardBalance = logCardRepository.findByBalance(payRequest.getCardId()); // 카드 잔액
        UUID cardId = payRequest.getCardId();

        if (cardBalance > cardAmount) {
            throw new BalanceException("잔액이 부족합니다.");
        }

        // 회원카드 금액변경
        int result = logCardRepository.updateAmount(cardId, cardAmount);

        if (result != 1) {
            throw new RuntimeException("데이터베이스에 잔액이 업데이트되지 못했습니다.");
        }

        return PayResponse.builder()
                .memberId(payRequest.getMemberId())
                .orderId(payRequest.getOrderId())
                .storeId(payRequest.getStoreId())
                .orderPrice(cardAmount)
                .build();
    }




}
