package com.project.starcoffee.kafka;

import com.project.starcoffee.dto.OrderIdDTO;
import com.project.starcoffee.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "order-rollback", groupId = "group-01")
    public void rollbackOrder(OrderIdDTO orderId) {
        log.error("======== [Rollback] order-rollback, orderId :{}======== ", orderId.getOrderId());
        orderService.requestCancel(orderId.getOrderId());
    }

}
