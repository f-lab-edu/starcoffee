package com.project.starcoffee.repository;

import com.project.starcoffee.dto.MemberCardDTO;
import com.project.starcoffee.dto.OrderDTO;
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
    public int saveOrder(UUID memberId, UUID cartId, Long storeId, int totalItemCount, int totalFinalPrice) {
        return orderMapper.saveOrder(memberId, cartId, storeId,
                totalItemCount, totalFinalPrice);
    }

    @Override
    public OrderDTO findByOrder(UUID cartId) {
        return orderMapper.findByOrder(cartId);
    }

}
