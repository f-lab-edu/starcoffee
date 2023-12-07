package com.project.starcoffee.kafka;

import com.project.starcoffee.dto.OrderIdDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@Component
public class OrderProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public OrderProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void rollbackOrder(OrderIdDTO orderId) {
        kafkaTemplate.send("order-rollback", orderId);
    }
}
