package com.project.starcoffee.controller;

import com.project.starcoffee.controller.response.order.CartResponse;
import com.project.starcoffee.controller.response.order.OrderResponse;
import com.project.starcoffee.dto.ItemDTO;
import com.project.starcoffee.service.CartService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public CartResponse insertCart(@RequestBody List<ItemDTO> itemDTO) {
        UUID cartId = cartService.insertCart(itemDTO);
        CartResponse cartResponse = CartResponse.builder()
                .cartId(cartId)
                .itemList(itemDTO)
                .build();
        return cartResponse;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> findCart(@PathVariable("id") UUID cartId) {
        List<ItemDTO> items = cartService.findCart(cartId);
        return items;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCart(@PathVariable("id") UUID cartId) {
        cartService.deleteItem(cartId);
    }

    @PostMapping("/order/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<OrderResponse>> requestOrder(@PathVariable("id") UUID cartId, HttpSession session) {
        return cartService.requestOrder(cartId, session);
    }
}