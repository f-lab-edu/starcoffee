package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.order.OrderRequest;
import com.project.starcoffee.dto.ItemDTO;
import com.project.starcoffee.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void runOrder(OrderRequest orderRequest, String memberId) {
        long storeId = orderRequest.getStoreId();
        List<ItemDTO> items = orderRequest.getItems().stream().collect(Collectors.toList());

        orderRepository.saveOrder();


    }
}
