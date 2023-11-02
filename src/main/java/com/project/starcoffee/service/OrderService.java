package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.dao.CartDAO;
import com.project.starcoffee.dto.*;
import com.project.starcoffee.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartDAO cartDAO;
    private WebClient webClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, CartDAO cartDAO) {
        this.orderRepository = orderRepository;
        this.cartDAO = cartDAO;
    }

    @PostConstruct
    public void initWebClient() {
        webClient = WebClient.create("http://localhost:8080");
    }

    public void Order(RequestOrderData orderRequest) {
        List<ItemDTO> itemList = orderRequest.getItemList();

        UUID memberId = UUID.fromString("7ddf7578-6a87-11ee-af50-ecf40330e8fa");
        UUID cartId = orderRequest.getCartId();
        Long storeId = orderRequest.getStoreId();
        int totalItemCount = itemList.stream().mapToInt(ItemDTO::getItemCount).sum();
        int totalFinalPrice = itemList.stream().mapToInt(ItemDTO::getFinalPrice).sum();

        int result = orderRepository.saveOrder(memberId, cartId, storeId, totalItemCount, totalFinalPrice);
        if (result != 1) {
            throw new RuntimeException("주문이 완료되지 못했습니다.");
        }

    }

    public List<ItemDTO> findOrderItemList(UUID cartId) {
        List<ItemDTO> items = cartDAO.findItem(cartId);
        return items;
    }

    public OrderDTO findOrder(UUID cartId) {
        OrderDTO OrderDTO = orderRepository.findByOrder(cartId);
        return OrderDTO;
    }

    public List<MemberCardDTO> findByMemberCard(UUID memberId) {
        List<MemberCardDTO> memberCards = orderRepository.findByMemberCard(memberId);
        return memberCards;
    }

    public Mono<PayResponse> requestPay(RequestPayData requestPayData) {
        UUID cartId = requestPayData.getCartId();

        // 주문정보 가져오기
        OrderDTO order = findOrder(cartId);
        UUID memberId = order.getMemberId();
        UUID orderId = order.getOrderId();
        long storeId = order.getStoreId();
        int finalPrice = order.getFinalPrice();

        // 회원의 카드가 맞는지 확인하기
        List<MemberCardDTO> memberCards = findByMemberCard(memberId);
        MemberCardDTO memberCardDTO = memberCards.stream().findFirst().get();
        UUID cardId = memberCardDTO.getCardId();

        // PayController 로 결제 요청
        return Mono.defer(() -> {
            return webClient.post()
                    .uri("/pay/paying")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new PayRequest(memberId, cardId, orderId,
                            storeId, finalPrice, Timestamp.valueOf(LocalDateTime.now())))
                    .retrieve()
                    .bodyToMono(PayResponse.class)
                    .subscribeOn(Schedulers.boundedElastic())
                    .publishOn(Schedulers.boundedElastic());
        });
    }
}
