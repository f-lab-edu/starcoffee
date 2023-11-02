package com.project.starcoffee.service;

import com.project.starcoffee.dao.CartDAO;
import com.project.starcoffee.dto.ItemDTO;
import com.project.starcoffee.dto.RequestOrderData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
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


    public Mono<List<ItemDTO>> requestOrder(UUID cartId) {
        return Mono.defer(() -> {
            List<ItemDTO> itemList = cartDAO.findItem(cartId);
            int storeId = 1;

            return webClient.post()
                    .uri("/order/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new RequestOrderData(cartId, storeId, itemList))
                    .retrieve()
                    .bodyToFlux(ItemDTO.class)
                    .collectList();
        });
//        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        String sessionId = attr.getSessionId();
//        log.info(sessionId);

// 세션 정보를 WebClient의 헤더에 추가
//        WebClient.RequestHeadersSpec<?> request = webClient.post()
//                .uri("/order/new")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(itemList)
//                .headers(headers -> headers.set("Cookie", "X-SESSION-ID=" + webSession.getId()));

        // API 호출
/*        List<ItemDTO> result = webClient.post()
                .uri("/order/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new RequestOrderData(cartId, itemList))
                .retrieve()
                .bodyToFlux(ItemDTO.class)
                .collectList()
                .block();

        return result;*/
    }


}
