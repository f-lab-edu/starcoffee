package com.project.starcoffee.controller;

import com.project.starcoffee.controller.response.order.OrderResponse;
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

    /**
     * 장바구니에서 주문요청을 하면 주문테이블에 주문을 저장한다.
     * @param orderRequest 장바구니 정보
     * @param session 세션
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse Order(@RequestBody RequestOrderData orderRequest, HttpSession session) {
        return orderService.Order(orderRequest, session);
    }

    /**
     * 저장된 주문을 조회한다.
     * @param orderId 주문 아이디
     * @return
     */
    @GetMapping("/orderList/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO findOrderList(@PathVariable("id") UUID orderId) {
        OrderDTO orderList = orderService.findByOrder(orderId);
        return orderList;
    }


    @PostMapping("/paying")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PayResponse> requestPay(@RequestBody RequestPayData requestPayData, HttpSession session) {
        return orderService.requestPay(requestPayData, session);
    }
}
