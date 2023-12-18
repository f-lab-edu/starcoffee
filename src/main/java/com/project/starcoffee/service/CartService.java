package com.project.starcoffee.service;

import com.project.starcoffee.controller.response.order.OrderResponse;
import com.project.starcoffee.dao.CartDAO;
import com.project.starcoffee.domain.store.StoreStatus;
import com.project.starcoffee.dto.ItemDTO;
import com.project.starcoffee.dto.RequestOrderData;
import com.project.starcoffee.redis.CartRedisDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CartService {
    // private final CartDAO cartDAO;
    private final CartRedisDAO cartRedisDAO;
    private final WebClient webClient;

    @Autowired
    public CartService(CartRedisDAO cartRedisDAO, WebClient webClient) {
        this.cartRedisDAO = cartRedisDAO;
        this.webClient = webClient;
    }


    public UUID insertCart(List<ItemDTO> itemDTO) {
        UUID newCartId = UUID.randomUUID();
        if (cartRedisDAO.duplicated(newCartId)) {
            throw new RuntimeException("기존의 장바구니가 존재합니다.");
        }

        cartRedisDAO.addCart(newCartId, itemDTO);
        return newCartId;
    }

    public List<ItemDTO> findCart(UUID cartId) {
        return cartRedisDAO.getCartItems(cartId);
    }

    public void deleteItem(UUID cartId) {
        cartRedisDAO.deleteCartItems(cartId);
    }


    public List<OrderResponse> requestOrder(UUID cartId) {
        List<ItemDTO> itemList = findCart(cartId);

        Long storeId = itemList.stream().findFirst().map(ItemDTO::getStoreId)
                .orElseThrow(() -> new RuntimeException("가게정보가 없습니다."));

        // 가게 오픈 여부 확인
        Mono<String> monoStoreStatus = webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/store/status")
                            .queryParam("storeId", storeId)
                            .build();
                })
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> log.error("error has occurred : {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .onErrorMap(e -> {
                    log.error("가게 오픈 여부확인 중 에러 발생: {}", e.getMessage());
                    return new RuntimeException("가게 오픈중에 오류가 발생했습니다.");
                });

        String storeStatus = monoStoreStatus.block();
        if (storeStatus.equals(StoreStatus.CLOSE.name())) {
            throw new RuntimeException("가게가 오픈되지 않아서 주문할 수 없습니다.");
        }

        // 장바구니 -> 주문 요청
        Mono<List<OrderResponse>> monoOrderResponseList = webClient.post()
                .uri("/order/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new RequestOrderData(storeId, itemList))
                .retrieve()
                .bodyToFlux(OrderResponse.class)
                .collectList()
                .doOnError(error -> log.error("error has occurred : {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .onErrorMap(e -> {
                    log.error("장바구니에서 주문 중 에러 발생: {}", e.getMessage());
                    return new RuntimeException("장바구니에서 주문 중에 오류가 발생했습니다.");
                });

        List<OrderResponse> orderResponseList = monoOrderResponseList.block();
        return orderResponseList;
    }

}
