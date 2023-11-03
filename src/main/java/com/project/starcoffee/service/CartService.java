package com.project.starcoffee.service;

import com.project.starcoffee.dao.CartDAO;
import com.project.starcoffee.dto.ItemDTO;
import com.project.starcoffee.dto.RequestOrderData;
import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CartService {

    private final CartDAO cartDAO;
    private WebClient webClient;

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
