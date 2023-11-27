package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.repository.PayRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class PayServiceTest {

    @Autowired
    private PayService payService;
    @Autowired
    private PayRepository payRepository;

    private PayRequest payRequest;


    @BeforeEach
    void init() {
        payRequest = PayRequest.builder()
                .memberId(UUID.randomUUID())
                .cardId(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .storeId(11L)
                .finalPrice(1234)
                .build();
    }


    @Test
    void 동시결제_분산락_적용_테스트() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    UUID uuid = payService.realPayment(payRequest);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Long price = payRepository.findPay(payRequest.getOrderId());
        assertThat(price).isEqualTo(1234L);

    }
}
