package com.project.starcoffee.repository.mybatis;

import com.project.starcoffee.domain.order.OrderStatus;
import com.project.starcoffee.dto.ItemDTO;
import com.project.starcoffee.repository.OrderRepository;
import com.project.starcoffee.repository.mybatis.mapper.OrderMapper;
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
    public int saveOrder(UUID memberId, List<ItemDTO> items, long storeId, int itemCount, int finalPrice) {
        return orderMapper.saveOrder(memberId, items, storeId, itemCount, finalPrice);
    }

}
