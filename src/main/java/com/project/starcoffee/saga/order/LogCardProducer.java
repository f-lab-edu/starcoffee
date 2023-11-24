package com.project.starcoffee.saga.order;

import com.project.starcoffee.controller.request.pay.BalanceRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class LogCardProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public LogCardProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void rollbackLogcard(BalanceRequest balanceRequest) {
        kafkaTemplate.send("logcard-rollback", balanceRequest.getCardId().toString(), balanceRequest);
    }
}
