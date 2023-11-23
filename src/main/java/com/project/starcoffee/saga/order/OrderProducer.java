package com.project.starcoffee.saga.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Slf4j
@Component
public class OrderProducer {

    private final KafkaTemplate kafkaTemplate;

    public OrderProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 주문번호 생성 이벤트로 "order-create" 토픽에 orderId 전송
     * @param orderId
     */
    public void order(UUID orderId) {
        kafkaTemplate.send("order-create", orderId);
    }
}
