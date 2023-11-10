package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.pay.BalanceRequest;
import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.dto.RequestPaySaveData;
import com.project.starcoffee.domain.pay.PayStatus;
import com.project.starcoffee.exception.BalanceException;
import com.project.starcoffee.repository.PayRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Slf4j
@Service
public class PayService {
    private final PayRepository payRepository;
    private final WebClient webClient;

    @Autowired
    public PayService(PayRepository payRepository, WebClient webClient) {
        this.payRepository = payRepository;
        this.webClient = webClient;
    }

    // 비동기
    @Transactional
    public PayResponse runPay(PayRequest payRequest) {
        long finalPrice = payRequest.getFinalPrice();    // 결제 금액
        UUID cardId = payRequest.getCardId();           // 카드 ID


        // 회원카드잔액 과 결제금액의 차이 확인
        Mono<LogCard> monoLogCard = webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/logcard/cardId")
                            .queryParam("cardId", cardId)
                            .build();
                })
                .retrieve()
                .bodyToMono(LogCard.class);

        LogCard memberCard = monoLogCard.block();
        if (memberCard.getCardBalance() < finalPrice) {
            throw new BalanceException("잔액이 부족합니다.");
        }

        // 결제 테이블 결제정보 저장
        RequestPaySaveData paySaveRequest = RequestPaySaveData.builder()
                .orderId(payRequest.getOrderId())
                .finalPrice(payRequest.getFinalPrice())
                .status(PayStatus.COMPLETE)
                .build();

        int result = payRepository.insertPay(paySaveRequest);
        if (result != 1) {
            throw new RuntimeException("결제가 완료되지 못했습니다.");
        }


        // 회원카드 금액변경
        Mono<Integer> resultLogCard = webClient.patch()
                .uri("/logcard/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BalanceRequest(cardId, finalPrice))
                .retrieve()
                .bodyToMono(Integer.class);
        resultLogCard.block();

        return PayResponse.builder()
                .memberId(payRequest.getMemberId())
                .orderId(payRequest.getOrderId())
                .storeId(payRequest.getStoreId())
                .orderPrice(finalPrice)
                .build();
    }

}
