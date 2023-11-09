package com.project.starcoffee.repository;

import com.project.starcoffee.dto.OrderDTO;
import com.project.starcoffee.dto.OrderItemDTO;
import com.project.starcoffee.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderMapper orderMapper;

    @Autowired
    public OrderRepositoryImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }


    @Override
    public int insertOrder(OrderDTO newOrder) {
        return orderMapper.insertOrder(newOrder);
    }

    @Override
    public int insertOrderItems(List<OrderItemDTO> orderItems) {
        return orderMapper.insertOrderItems(orderItems);
    }

    @Override
    public OrderDTO findByOrder(UUID orderId) {
        return orderMapper.findByOrder(orderId);
    }

}
