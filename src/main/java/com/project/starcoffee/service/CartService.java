package com.project.starcoffee.service;

import com.project.starcoffee.dao.CartDAO;
import com.project.starcoffee.dto.ItemDTO;
import com.project.starcoffee.dto.RequestOrderData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CartService {
    private final CartDAO cartDAO;
    private WebClient webClient;
    private static final long SCHEDULE_DELETE_CART_SECOND = 86400;

    @Autowired
    public CartService(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    @PostConstruct
    public void initWebClient() {
        webClient = WebClient.create("http://localhost:8080");
    }

    public UUID insertCart(List<ItemDTO> itemDTO) {
        UUID newCartId = UUID.randomUUID();
        if (cartDAO.duplicatedId(newCartId)) {
            throw new RuntimeException("기존의 장바구니가 존재합니다.");
        }

        UUID cartId = cartDAO.saveItem(newCartId, itemDTO);
        return cartId;
    }

    public List<ItemDTO> findCart(UUID cartId) {
        List<ItemDTO> items = cartDAO.findItem(cartId);
        return items;
    }

    public void deleteItem(UUID cartId) {
        cartDAO.deleteItem(cartId);
    }

    /**
     * 일주일동안 주문하지 않은 장바구니는 24시간 뒤에 자동으로 삭제된다.
     */
    @Scheduled(fixedRate = SCHEDULE_DELETE_CART_SECOND)
    public void deleteBySchedule() {
        log.info("일주일 동안 담긴 상품은 자동 으로 삭제 처리");
        cartDAO.autoDeleteItem();
    }


    public Mono<List<ItemDTO>> requestOrder(UUID cartId, HttpSession session) {
        String sessionId = session.getId();

        return Mono.defer(() -> {
            List<ItemDTO> itemList = cartDAO.findItem(cartId);
            int storeId = 1;

            return webClient.post()
                    .uri("/order/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie("JSESSIONID", sessionId)
                    .bodyValue(new RequestOrderData(cartId, storeId, itemList))
                    .retrieve()
                    .bodyToFlux(ItemDTO.class)
                    .collectList();
        });
    }

}
