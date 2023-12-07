package com.project.starcoffee.kafka;

import com.project.starcoffee.dto.RollbackRequest;
import com.project.starcoffee.service.LogCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogCardConsumer {

    private final LogCardService logCardService;

    @KafkaListener(topics = "logcard-rollback", groupId = "group-01")
    public void rollbackLogCard(RollbackRequest rollbackRequest) {
        log.error("======== [Rollback] logcard-rollback, cardId :{}======== ", rollbackRequest.getCardId());
        logCardService.requestCancel(rollbackRequest);
    }
}
