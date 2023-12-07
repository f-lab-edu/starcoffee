package com.project.starcoffee.service;

import com.project.starcoffee.aop.distributeLock.DistributedLock;
import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.order.OrderResponse;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.controller.response.pay.PaymentResponse;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.dto.*;
import com.project.starcoffee.kafka.OrderProducer;
import com.project.starcoffee.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;
    private final WebClient webClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderProducer orderProducer, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.orderProducer = orderProducer;
        this.webClient = webClient;
    }

    @Transactional
    @DistributedLock(key = "#strMemberId")
    public OrderResponse newOrder(RequestOrderData orderRequest, String strMemberId) {
        List<ItemDTO> itemList = orderRequest.getItemList();
        UUID memberId = UUID.fromString(strMemberId);
        Long storeId = orderRequest.getStoreId();

        // 주문 테이블에 주문 정보 저장
        OrderDTO newOrder = OrderDTO.builder()
                .memberId(memberId)
                .storeId(storeId)
                .itemCount(itemList.stream().mapToInt(ItemDTO::getItemCount).sum())
                .finalPrice(itemList.stream().mapToInt(ItemDTO::getFinalPrice).sum())
                .build();

        int result = orderRepository.insertOrder(newOrder);
        if (result != 1) {
            throw new RuntimeException("주문이 완료되지 못했습니다.");
        }

        // 주문 아이템 테이블에 세부 주문 정보 저장
        UUID orderId = newOrder.getOrderId();
        List<OrderItemDTO> orderItems = itemList.stream()
                .map(item -> OrderItemDTO.builder()
                        .orderId(orderId)
                        .itemId(item.getItemId())
                        .itemName(item.getItemName())
                        .itemPrice(item.getItemPrice())
                        .itemType(item.getItemType())
                        .itemSize(item.getItemSize())
                        .cupSize(item.getCupSize())
                        .build()
                ).collect(Collectors.toList());
        orderRepository.insertOrderItems(orderItems);

        return OrderResponse.builder()
                .orderId(orderId)
                .memberId(newOrder.getMemberId())
                .storeId(newOrder.getStoreId())
                .itemCount(newOrder.getItemCount())
                .finalPrice(newOrder.getFinalPrice())
                .orderItems(orderItems)
                .build();
    }

    public OrderDTO findByOrder(UUID orderId) {
        Optional<OrderDTO> orderOptional = orderRepository.findByOrder(orderId);
        return orderOptional.orElseThrow(() -> new RuntimeException("주문 리스트가 없습니다."));
    }

    @Transactional
    public PaymentResponse requestPay(RequestPayData requestPayData) {
        UUID orderId = requestPayData.getOrderId();
        UUID requestCardId = requestPayData.getCardId();

        // 주문정보 가져오기
        OrderDTO order = findByOrder(orderId);
        UUID memberId = order.getMemberId();
        long storeId = order.getStoreId();
        long finalPrice = order.getFinalPrice();

        // 주문ID 객체 (보상 트랜잭션)
        OrderIdDTO orderIdDTO = OrderIdDTO.builder()
                .orderId(orderId)
                .build();

        // 회원 카드 확인
        Mono<LogCard> monoLogCard = webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/logcard/cardId")
                            .queryParam("orderId", orderId)
                            .queryParam("memberId",memberId)
                            .queryParam("cardId",requestCardId)
                            .build();
                })
                .retrieve()
                .bodyToMono(LogCard.class)
                .doOnError(error -> log.error("error has occurred : {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .onErrorMap(e -> {
                    log.error("회원카드 여부 중 에러 발생: {}", e.getMessage());
                    // 보상트랜잭션 이벤트 발행(주문취소)
                    orderProducer.rollbackOrder(orderIdDTO);
                    return new RuntimeException("회원카드 여부 중에 오류가 발생했습니다.");
                });

        // 요청한 카드와 회원이 등록한 카드가 일치하는지 확인
        LogCard memberCard = monoLogCard.block();
        if (!memberCard.getCardId().equals(requestPayData.getCardId())) {
            // 보상 트랜잭션 이벤트 발행(주문취소)
            orderProducer.rollbackOrder(orderIdDTO);
            throw new RuntimeException("선택하신 카드와 회원의 카드가 일치하지 않습니다.");
        }

        // PayController 로 결제 요청
        UUID cardId = memberCard.getCardId();
        Mono<PaymentResponse> paymentResponseMono = webClient.post()
                .uri("/pay/paying/card")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PayRequest(memberId, cardId, orderId,
                        storeId, finalPrice, Timestamp.valueOf(LocalDateTime.now())))
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .doOnError(error -> log.error("error has occurred : {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .onErrorMap(e -> {
                    log.error("결제진행 중 에러 발생: {}", e.getMessage());
                    // 보상 트랜잭션 이벤트 발행(주문취소)
                    orderProducer.rollbackOrder(orderIdDTO);
                    return new RuntimeException("결제진행 중에 오류가 발생했습니다.");
                });

        PaymentResponse paymentResponse = paymentResponseMono.block();
        return paymentResponse;
    }

    @Transactional
    @Retryable(value = DataAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void requestCancel(UUID orderId) {
        try {
            int result = orderRepository.cancelOrder(orderId);
            if (result != 1) {
                throw new RuntimeException("주문취소가 실패했습니다.");
            }
        } catch (DataAccessException e) {
            log.error("주문 취소 중 재시도 중 에러 발생", e.getMessage());
        } catch (RuntimeException e) {
            log.error("주문 취소 중 주문취소 자체 에러발생", e.getMessage());
        }


        log.info("{}번 주문ID -> 주문취소로 업데이트", orderId);
    }


}
