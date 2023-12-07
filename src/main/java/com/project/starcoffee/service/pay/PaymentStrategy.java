package com.project.starcoffee.service.pay;

import com.project.starcoffee.controller.request.pay.PayRequest;

import java.util.UUID;

public interface PaymentStrategy {
    void checkCardBalance(UUID memberId, UUID cardId, long finalPrice, UUID orderId);
    void processPayment(PayRequest payRequest);
    void updateCardBalance(UUID cardId, long finalPrice);
}
