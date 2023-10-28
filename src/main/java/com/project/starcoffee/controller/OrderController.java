package com.project.starcoffee.controller;

import com.project.starcoffee.controller.request.order.OrderRequest;
import com.project.starcoffee.controller.response.order.CartResponse;
import com.project.starcoffee.dto.ItemDTO;
import com.project.starcoffee.service.OrderService;
import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public void doOrder(@RequestBody OrderRequest orderRequest, HttpSession session) {
        String member = SessionUtil.getMemberId(session);
        orderService.runOrder(orderRequest, member);
    }
}
