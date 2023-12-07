package com.project.starcoffee.service.pay;

import com.project.starcoffee.aop.distributeLock.DistributedLock;
import com.project.starcoffee.controller.request.pay.BalanceRequest;
import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.domain.pay.PayStatus;
import com.project.starcoffee.dto.RequestPaySaveData;
import com.project.starcoffee.exception.BalanceException;
import com.project.starcoffee.repository.PayRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
public class DefaultPaymentStrategy implements PaymentStrategy {
    private final WebClient webClient;
    private final PayRepository payRepository;

    @Autowired
    public DefaultPaymentStrategy(WebClient webClient, PayRepository payRepository) {
        this.webClient = webClient;
        this.payRepository = payRepository;
    }

    /**
     * 회원카드 잔액과 결제금액의 차이 확인
     * @param memberId
     * @param cardId
     * @param finalPrice
     * @return
     */
    @Override
    @DistributedLock(key = "#cardId")
    public void checkCardBalance(UUID memberId, UUID cardId, long finalPrice, UUID orderId) {
        Mono<LogCard> monoLogCard = webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/logcard/cardId")
                            .queryParam("memberId", memberId)
                            .queryParam("cardId", cardId)
                            .queryParam("orderId", orderId)
                            .build();
                })
                .retrieve()
                .bodyToMono(LogCard.class)
                .doOnError(error -> log.error("error has occurred : {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .onErrorMap(e -> {
                    log.error("회원카드 잔액확인 중 에러 발생: {}", e.getMessage());
                    return new RuntimeException("회원카드 잔액확인 중에 오류가 발생했습니다.");
                });
        LogCard logCard = monoLogCard.block();
        if (logCard.getCardBalance() < finalPrice) {
            throw new BalanceException("잔액이 부족합니다.");
        }
    }

    /**
     * 실제 결제가 이루어지는 메서드
     * (결제 테이블에 결제정보가 저장되는 것으로 구현 / 취소 시 -결제 금액이 저장)
     * @param payRequest
     */
    @Override
    @DistributedLock(key = "#payRequest.getOrderId()")
    public void processPayment(PayRequest payRequest) {
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
    }

    /**
     * 실제로 결제가 완료되었기 때문에 회원카드에 결제금액이 차감된다.
     * @param cardId
     * @param finalPrice
     */
    @Override
    @DistributedLock(key = "#cardId")
    public void updateCardBalance(UUID cardId, long finalPrice) {
        Mono<Integer> resultLogCard = webClient.patch()
                .uri("/logcard/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BalanceRequest(cardId, finalPrice))
                .retrieve()
                .bodyToMono(Integer.class)
                .doOnError(error -> log.error("error has occurred : {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .onErrorMap(e -> {
                    log.error("회원카드 금액변경 중 에러 발생: {}", e.getMessage());
                    return new RuntimeException("회원카드 금액변경 중에 오류가 발생했습니다.");
                });
        resultLogCard.block();
    }
}
