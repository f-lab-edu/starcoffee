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

import javax.servlet.http.HttpSession;
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
    public void Order(@RequestBody RequestOrderData orderRequest, HttpSession session) {
        orderService.Order(orderRequest, session);
    }

    @GetMapping("/itemList/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> findOrderItemList(@PathVariable("id") UUID cartId) {
        List<ItemDTO> itemList = orderService.findOrderItemList(cartId);
        return itemList;
    }

    @GetMapping("/orderList/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO findOrderList(@PathVariable("id") UUID cartId) {
        OrderDTO orderList = orderService.findOrder(cartId);
        return orderList;
    }

    @PostMapping("/paying")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PayResponse> requestPay(@RequestBody RequestPayData requestPayData, HttpSession session) {
        return orderService.requestPay(requestPayData, session);
    }
}
