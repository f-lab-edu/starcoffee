package com.project.starcoffee.saga.logcard;

import com.project.starcoffee.controller.request.pay.BalanceRequest;
import com.project.starcoffee.service.LogCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogCardConsumer {

    private final LogCardService logCardService;

    @KafkaListener(topics = "logcard-rollback", groupId = "group-01")
    public void rollbackLogCard(BalanceRequest balanceRequest) {
        log.error("======== [Rollback] logcard-rollback, cardId :{}======== ", balanceRequest.getCardId());
        logCardService.requestCancel(balanceRequest);
    }
}
