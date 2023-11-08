package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.dao.CartDAO;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.dto.*;
import com.project.starcoffee.repository.OrderRepository;

import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
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

    public void Order(RequestOrderData orderRequest, HttpSession session) {
        List<ItemDTO> itemList = orderRequest.getItemList();
        UUID memberId = UUID.fromString(SessionUtil.getMemberId(session));

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

    public Mono<PayResponse> requestPay(RequestPayData requestPayData, HttpSession session) {
        String sessionId = session.getId();
        UUID cartId = requestPayData.getCartId();

        // 주문정보 가져오기
        OrderDTO order = findOrder(cartId);
        UUID memberId = order.getMemberId();
        UUID orderId = order.getOrderId();
        long storeId = order.getStoreId();
        int finalPrice = order.getFinalPrice();

        // 회원 카드 확인
        Mono<LogCard> monoLogCard = webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/logcard")
                            .build();
                })
                .cookie("JSESSIONID", sessionId)
                .retrieve()
                .bodyToMono(LogCard.class)
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.boundedElastic());

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
                    .cookie("JSESSIONID", sessionId)
                    .bodyValue(new PayRequest(memberId, cardId, orderId,
                            storeId, finalPrice, Timestamp.valueOf(LocalDateTime.now())))
                    .retrieve()
                    .bodyToMono(PayResponse.class)
                    .subscribeOn(Schedulers.boundedElastic())
                    .publishOn(Schedulers.boundedElastic());
        });
    }
}
