package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.card.CardNumberRequest;
import com.project.starcoffee.controller.request.pay.BalanceRequest;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.dto.OrderIdDTO;
import com.project.starcoffee.dto.RollbackRequest;
import com.project.starcoffee.kafka.OrderProducer;
import com.project.starcoffee.repository.LogCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class LogCardService {
    private final LogCardRepository logCardRepository;
    private final OrderProducer orderProducer;
    private final WebClient webClient;

    @Autowired
    public LogCardService(LogCardRepository logCardRepository, OrderProducer orderProducer, WebClient webClient) {
        this.logCardRepository = logCardRepository;
        this.orderProducer = orderProducer;
        this.webClient = webClient;
    }

    public List<LogCard> findByMemberId(String strMemberId) {
        UUID memberId = UUID.fromString(strMemberId);
        List<LogCard> cardList = logCardRepository.findByMemberId(memberId);

        if (cardList.isEmpty()) {
            throw new RuntimeException("회원으로 등록된 카드를 찾을 수 없습니다.");
        }

        return cardList;
    }

    public LogCard findByCardId(UUID memberId, UUID cardId, UUID orderId) {
        Optional<LogCard> cardInfoOptional = logCardRepository.findByCardId(memberId, cardId);

        cardInfoOptional.orElseThrow(()-> {
            // 보상 트랜잭션 이벤트 발행 (주문취소)
            OrderIdDTO orderIdDTO = OrderIdDTO.builder()
                    .orderId(orderId)
                    .build();
            orderProducer.rollbackOrder(orderIdDTO);

            throw new RuntimeException("회원으로 등록된 카드를 찾을 수 없습니다.");
        });

        return cardInfoOptional.get();
    }


    public Mono<Card> requestFindCard(CardNumberRequest cardNumberRequest) {
        // 스타카드가 존재하는지 확인
        Mono<Card> cardMono = webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/cards/find")
                            .queryParam("cardNumber", cardNumberRequest.getCardNumber())
                            .build();
                })
                .retrieve()
                .bodyToMono(Card.class)
                .doOnError(error -> log.error("error has occurred : {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .onErrorMap(e -> {
                    log.error("스타카드 존재여부 중 에러 발생: {}", e.getMessage());
                    return new RuntimeException("스타카드 존재여부 중에 오류가 발생했습니다.");
                });

/* 비동기 시 나타나는 에러 확인
        No thread-bound request found:
        Are you referring to request attributes outside of an actual web request,
        or processing a request outside of the originally receiving thread?
        If you are actually operating within a web request and still receive this message,
                your code is probably running outside of DispatcherServlet:
        In this case, use RequestContextListener or RequestContextFilter to expose the current request.*/

        Card card = cardMono.block();
        UUID cardId = card.getCardId();

        // 입력된 카드가 다른 회원에 등록되어 있는지 확인
        if (duplicatedCard(cardId)) {
            throw new RuntimeException("카드가 이미 등록되어 있습니다.");
        }

        return cardMono;
    }

    public Card enrollCard(CardNumberRequest cardNumberRequest, String strMemberId) {
        Mono<Card> cardMono = requestFindCard(cardNumberRequest);

        Mono<Card> cardMonoResult = cardMono.flatMap(card -> {
            UUID cardId = card.getCardId();
            int cardBalance = card.getCardBalance();
            UUID memberId = UUID.fromString(strMemberId);

            // 카드를 카드이력 테이블에 등록
            logCardRepository.enrollCard(memberId, cardId, cardBalance);

            return Mono.just(card);
        });

        return cardMonoResult.block();
    }

    public boolean duplicatedCard(UUID cardId) {
        return logCardRepository.duplicatedCard(cardId);
    }


    public int findByBalance(UUID cardId) {
        return logCardRepository.findByBalance(cardId);
    }

    public Integer withDrawAmount(BalanceRequest balanceRequest) {
        UUID cardId = balanceRequest.getCardId();
        long cardAmount = balanceRequest.getFinalPrice();

        int result = logCardRepository.withDrawAmount(cardId, cardAmount);
        if (result != 1) {
            throw new RuntimeException("데이터베이스에 잔액이 업데이트되지 못했습니다.");
        }
        return result;
    }


    @Transactional
    @Retryable(value = DataAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void requestCancel(RollbackRequest rollbackRequest) {
        UUID cardId = rollbackRequest.getCardId();
        long cardAmount = rollbackRequest.getFinalPrice();

        try {
            int result = logCardRepository.requestCancel(cardId, cardAmount);
            if (result != 1) {
                throw new RuntimeException("데이터베이스에 취소금액이 업데이트되지 못했습니다.");
            }
        } catch (DataAccessException e) {
            log.error("결제금액 복원 중 재시도 중 에러 발생", e.getMessage());
        } catch (RuntimeException e) {
            log.error("결제금액 업데이트 중 자체 에러발생", e.getMessage());
        }

        log.info("{}번 카드ID와 {}원 -> 카드취소 업데이트", cardId, cardAmount);
    }
}
