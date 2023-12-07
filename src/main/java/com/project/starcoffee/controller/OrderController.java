package com.project.starcoffee.controller;

import com.project.starcoffee.aop.session.SessionMemberId;
import com.project.starcoffee.controller.response.order.OrderResponse;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.controller.response.pay.PaymentResponse;
import com.project.starcoffee.dto.*;
import com.project.starcoffee.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
     *
     * @param orderRequest 장바구니 정보
     * @param strMemberId  aop -> 회원 아이디
     */
    @PostMapping("/new")
    @SessionMemberId
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse newOrder(@RequestBody RequestOrderData orderRequest, String strMemberId) {
        return orderService.newOrder(orderRequest, strMemberId);
    }

    /**
     * 저장된 주문을 조회한다.
     *
     * @param orderId 주문 아이디
     * @return
     */
    @GetMapping("/orderList/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO findOrderList(@PathVariable("id") UUID orderId) {
        OrderDTO orderList = orderService.findByOrder(orderId);
        return orderList;
    }

    /**
     * 주문에서 결제요청을 한다.
     *
     * @param requestPayData 결제요청 데이터
     * @return
     */
    @PostMapping("/paying")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse requestPay(@RequestBody RequestPayData requestPayData) {
        return orderService.requestPay(requestPayData);
    }

    @PostMapping("/cancelling")
    @SessionMemberId
    @ResponseStatus(HttpStatus.OK)
    public void requestCancel(@RequestParam UUID orderId) {
        orderService.requestCancel(orderId);
    }
}
