package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.order.OrderRequest;
import com.project.starcoffee.dao.CartDAO;
import com.project.starcoffee.domain.item.Item;
import com.project.starcoffee.domain.order.OrderStatus;
import com.project.starcoffee.dto.ItemDTO;
import com.project.starcoffee.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void runOrder(OrderRequest orderRequest, String member) {
        long storeId = orderRequest.getStoreId();
        UUID memberId = UUID.fromString(member);
        List<ItemDTO> items = orderRequest.getItems();

//        List<Long> itemId = orderRequest.getItems().stream()
//                .map(ItemDTO::getItemId)
//                .collect(Collectors.toList());

        int itemCount = orderRequest.getItemCount();
        int finalPrice = orderRequest.getFinalPrice();

        orderRepository.saveOrder(memberId, items, storeId, itemCount, finalPrice);
    }
}
