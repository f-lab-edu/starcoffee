package com.project.starcoffee.kafka;

import com.project.starcoffee.dto.RollbackRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class LogCardProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public LogCardProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void rollbackLogcard(RollbackRequest rollbackRequest) {
        kafkaTemplate.send("logcard-rollback", rollbackRequest);
    }
}
