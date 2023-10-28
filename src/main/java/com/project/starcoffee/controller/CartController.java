package com.project.starcoffee.controller;

import com.project.starcoffee.controller.response.order.CartResponse;
import com.project.starcoffee.dto.ItemDTO;
import com.project.starcoffee.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/cart")
    @ResponseStatus(HttpStatus.OK)
    public CartResponse insertCart(@RequestBody List<ItemDTO> itemDTO) {
        UUID cartId = cartService.insertCart(itemDTO);
        CartResponse cartResponse = new CartResponse(cartId, itemDTO);
        return cartResponse;
    }

    @PatchMapping("/cart/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> findCart(@PathVariable("id") UUID cartId) {
        List<ItemDTO> items = cartService.findCart(cartId);
        return items;
    }

    @DeleteMapping("/cart/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCart(@PathVariable("id") UUID cartId) {
        cartService.deleteItem(cartId);
    }



}
