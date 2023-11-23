package com.project.starcoffee.saga.payment;

import com.project.starcoffee.controller.request.pay.CancelRequest;
import com.project.starcoffee.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {
    private final PayService payService;

    @KafkaListener(topics = "payment-rollback", groupId = "group-01")
    public void rollbackPayment(CancelRequest cancelRequest) {
        log.error("======== [Rollback] payment-rollback, paymentId :{}======== ", cancelRequest.getPaymentId());
        payService.runCancel(cancelRequest);
    }
}
