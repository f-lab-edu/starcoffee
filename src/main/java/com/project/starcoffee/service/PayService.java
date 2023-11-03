package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.pay.BalanceRequest;
import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.exception.BalanceException;
import com.project.starcoffee.repository.LogCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Slf4j
@Service
public class PayService {
    private final LogCardRepository logCardRepository;
    private WebClient webClient;

    @Autowired
    public PayService(LogCardRepository logCardRepository) {
        this.logCardRepository = logCardRepository;
    }

    @PostConstruct
    public void initWebClient() {
        webClient = WebClient.create("http://localhost:8080");
    }


    // 비동기
    @Transactional
    public PayResponse runPay(PayRequest payRequest,  HttpSession session) {
        String sessionId = session.getId();
        int finalPrice = payRequest.getFinalPrice();    // 결제 금액
        UUID cardId = payRequest.getCardId();// 카드 ID

        // 회원의 카드잔액 확인
        Mono<Integer> monoBalance = webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/logcard/balance")
                            .queryParam("cardId", payRequest.getCardId().toString())
                            .build();
                })
                .cookie("JSESSIONID", sessionId)
                .retrieve()
                .bodyToMono(int.class)
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.boundedElastic());

        // 카드 잔액
        Integer cardBalance = monoBalance.block();
        log.info(cardBalance.toString());

        if (cardBalance < finalPrice) {
            throw new BalanceException("잔액이 부족합니다.");
        }

        // 회원카드 금액변경
        Mono<Void> monoResult = webClient.patch()
                .uri("/logcard/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BalanceRequest(cardId, finalPrice))
                .cookie("JSESSIONID", sessionId)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.boundedElastic());

        // 회원카드 금액변경
//        int result = logCardRepository.updateAmount(cardId, finalPrice);
//        if (result != 1) {
//            throw new RuntimeException("데이터베이스에 잔액이 업데이트되지 못했습니다.");
//        }

        return PayResponse.builder()
                .memberId(payRequest.getMemberId())
                .orderId(payRequest.getOrderId())
                .storeId(payRequest.getStoreId())
                .orderPrice(finalPrice)
                .build();
    }




}
