package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.pay.BalanceRequest;
import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.exception.BalanceException;
import com.project.starcoffee.repository.LogCardRepository;
import com.project.starcoffee.repository.PayRepository;
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
    private final PayRepository payRepository;
    private WebClient webClient;

    @Autowired
    public PayService(PayRepository payRepository) {
        this.payRepository = payRepository;
    }

    @PostConstruct
    public void initWebClient() {
        webClient = WebClient.create("http://localhost:8080");
    }

    // 비동기
    @Transactional
    public PayResponse runPay(PayRequest payRequest, HttpSession session) {
        String sessionId = session.getId();
        int finalPrice = payRequest.getFinalPrice();    // 결제 금액
        UUID cardId = payRequest.getCardId();           // 카드 ID

        // 회원 카드 확인
        Mono<LogCard> monoLogCard = webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/logcard")
                            .build();
                })
                .cookie("JSESSIONID", sessionId)
                .retrieve()
                .bodyToMono(LogCard.class)
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.boundedElastic());

        // 회원카드잔액 과 결제금액의 차이 확인
        LogCard memberCard = monoLogCard.block();
        if (memberCard.getCardBalance() < finalPrice) {
            throw new BalanceException("잔액이 부족합니다.");
        }

        // 회원카드 금액변경
        Mono<Integer> result = webClient.patch()
                .uri("/logcard/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie("JSESSIONID", sessionId)
                .bodyValue(new BalanceRequest(cardId, finalPrice))
                .retrieve()
                .bodyToMono(Integer.class);
        result.block();

        return PayResponse.builder()
                .memberId(payRequest.getMemberId())
                .orderId(payRequest.getOrderId())
                .storeId(payRequest.getStoreId())
                .orderPrice(finalPrice)
                .build();
    }

}
