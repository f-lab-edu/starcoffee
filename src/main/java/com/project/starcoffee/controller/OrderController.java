package com.project.starcoffee.controller;

import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.dto.*;
import com.project.starcoffee.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public void Order(@RequestBody RequestOrderData orderRequest) {
        orderService.Order(orderRequest);
    }

    @GetMapping("/find/itemList/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> findOrderItemList(@PathVariable("id") UUID cartId) {
        List<ItemDTO> itemList = orderService.findOrderItemList(cartId);
        return itemList;
    }

    @GetMapping("/find/orderList/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO findOrderList(@PathVariable("id") UUID cartId) {
        OrderDTO orderList = orderService.findOrder(cartId);
        return orderList;
    }

    @GetMapping("/find/card/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<MemberCardDTO> findMemberCard(@PathVariable("id") UUID memberId) {
        return orderService.findByMemberCard(memberId);
    }

    @PostMapping("/paying")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PayResponse> requestPay(@RequestBody RequestPayData requestPayData) {
        return orderService.requestPay(requestPayData);
    }
}
