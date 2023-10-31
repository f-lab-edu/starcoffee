package com.project.starcoffee.service;

import com.project.starcoffee.config.login.LoginRequired;
import com.project.starcoffee.controller.request.order.OrderRequest;
import com.project.starcoffee.dao.CartDAO;
import com.project.starcoffee.dto.ItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        UUID cartId = cartDAO.saveItem(itemDTO);
        return cartId;
    }

    public List<ItemDTO> findCart(UUID cartId) {
        List<ItemDTO> items = cartDAO.findItem(cartId);
        return items;
    }

    public void deleteItem(UUID cartId) {
        cartDAO.deleteItem(cartId);
    }

    @LoginRequired
    public List<ItemDTO> requestOrder(UUID cartId) {
        List<ItemDTO> itemList = cartDAO.findItem(cartId);

        List<ItemDTO> result = webClient.post()
                .uri("/order/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemList)
                .retrieve()
                .bodyToFlux(ItemDTO.class)
                .collectList()
                .block();

        return result;
    }


}
