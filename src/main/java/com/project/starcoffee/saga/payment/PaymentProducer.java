package com.project.starcoffee.saga.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class PaymentProducer {

    private final KafkaTemplate kafkaTemplate;

    public PaymentProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void rollbackOrder(UUID orderId) {
        kafkaTemplate.send("order-rollback", orderId);
    }
}
