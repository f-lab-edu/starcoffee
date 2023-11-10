package com.project.starcoffee.repository;

import com.project.starcoffee.dto.OrderDTO;
import com.project.starcoffee.dto.OrderItemDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository {
    int insertOrder(OrderDTO newOrder);

    int insertOrderItems(List<OrderItemDTO> orderItems);

    Optional<OrderDTO> findByOrder(UUID orderId);

}
