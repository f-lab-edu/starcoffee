package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.order.OrderResponse;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.dto.*;
import com.project.starcoffee.repository.OrderRepository;

import com.project.starcoffee.saga.order.OrderProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
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

//    @Transactional
    public OrderResponse Order(RequestOrderData orderRequest, String strMemberId) {
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

        // 주문번호 생성 이벤트 호출
        orderProducer.order(orderId);

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

//    @Transactional
    @KafkaListener(topics = "order-create", groupId = "group-01")
    public Mono<PayResponse> requestPay(RequestPayData requestPayData) {
        UUID orderId = requestPayData.getOrderId();
        UUID requestCardId = requestPayData.getCardId();

        // 주문정보 가져오기
        OrderDTO order = findByOrder(orderId);
        UUID memberId = order.getMemberId();
        long storeId = order.getStoreId();
        long finalPrice = order.getFinalPrice();

        // 회원 카드 확인
        Mono<LogCard> monoLogCard = webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/logcard/cardId")
                            .queryParam("memberId",memberId)
                            .queryParam("cardId",requestCardId)
                            .build();
                })
                .retrieve()
                .bodyToMono(LogCard.class);

        // 요청한 카드와 회원이 등록한 카드가 일치하는지 확인
        LogCard memberCard = monoLogCard.block();
        if (!memberCard.getCardId().equals(requestPayData.getCardId())) {
            throw new RuntimeException("선택하신 카드와 회원의 카드가 일치하지 않습니다.");
        }

        // PayController 로 결제 요청
        return Mono.defer(() -> {
            UUID cardId = memberCard.getCardId();
            return webClient.post()
                    .uri("/pay/paying")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new PayRequest(memberId, cardId, orderId,
                            storeId, finalPrice, Timestamp.valueOf(LocalDateTime.now())))
                    .retrieve()
                    .bodyToMono(PayResponse.class);
        });
    }

    //@Transactional
    public void requestCancel(UUID orderId, String strMemberId) {
        UUID memberId = UUID.fromString(strMemberId);
        int result = orderRepository.cancelOrder(orderId, memberId);
        if (result != 1) {
            throw new RuntimeException("주문취소가 실패했습니다.");
        }

        log.info("{ } 번 주문ID -> 주문취소로 변경", orderId);

    }


}
