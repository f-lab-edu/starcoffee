package com.project.starcoffee.saga.payment;

import com.project.starcoffee.dto.OrderIDDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PaymentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public PaymentProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void rollbackOrder(OrderIDDTO orderIDDTO) {
        kafkaTemplate.send("order-rollback", orderIDDTO);
    }
}
