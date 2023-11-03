package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.card.CardNumberRequest;
import com.project.starcoffee.controller.request.pay.BalanceRequest;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.repository.LogCardRepository;
import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LogCardService {
    private final LogCardRepository logCardRepository;
    private WebClient webClient;

    @Autowired
    public LogCardService(LogCardRepository logCardRepository) {
        this.logCardRepository = logCardRepository;
    }

    @PostConstruct
    public void initWebClient() {
        webClient = WebClient.create("http://localhost:8080");
    }

    public LogCard findByCard(String member) {
        UUID memberId = UUID.fromString(member);
        Optional<LogCard> cardInfoOptional = logCardRepository.findByCard(memberId);

        cardInfoOptional.ifPresentOrElse(card -> {},
                ()-> { throw new RuntimeException("회원으로 등록된 카드를 찾을 수 없습니다."); }
        );

        return cardInfoOptional.get();
    }


    public Mono<Card> requestFindCard(CardNumberRequest cardNumberRequest, HttpSession session) {
        String sessionId = session.getId();

        return webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/cards/find")
                            .queryParam("cardNumber", cardNumberRequest.getCardNumber())
                            .build();
                })
                .cookie("JSESSIONID", sessionId)
                .retrieve()
                .bodyToMono(Card.class)
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.boundedElastic());

    }

    public Card enrollCard(CardNumberRequest cardNumberRequest, HttpSession session) {
        Mono<Card> cardMono = requestFindCard(cardNumberRequest, session);

        Mono<Card> cardMonoResult = cardMono.flatMap(card -> {
            UUID cardId = card.getCardId();
            int cardBalance = card.getCardBalance();
            UUID memberId = UUID.fromString(SessionUtil.getMemberId(session));

            // 입력된 카드가 다른 회원에 등록되어 있는지 확인
            if (duplicatedCard(cardId)) {
                throw new RuntimeException("카드가 이미 등록되어 있습니다.");
            }

            // 카드를 카드이력 테이블에 등록
            logCardRepository.enrollCard(memberId, cardId, cardBalance);

            return Mono.just(card);
        });

        return cardMonoResult.block();
    }

    public boolean duplicatedCard(UUID cardId) {
        return logCardRepository.duplicatedCard(cardId);
    }


    public int findByBalance(String cardId) {
        UUID cardAccount = UUID.fromString(cardId);
        return logCardRepository.findByBalance(cardAccount);
    }

    public void withDrawAmount(BalanceRequest balanceRequest) {
        UUID cardId = balanceRequest.getCardId();
        int finalPrice = balanceRequest.getFinalPrice();

        int result = logCardRepository.updateAmount(cardId, finalPrice);
        if (result != 1) {
            throw new RuntimeException("데이터베이스에 잔액이 업데이트되지 못했습니다.");
        }
    }
}
