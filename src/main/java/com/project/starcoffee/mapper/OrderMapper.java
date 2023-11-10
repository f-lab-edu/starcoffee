package com.project.starcoffee.mapper;

import com.project.starcoffee.dto.OrderDTO;
import com.project.starcoffee.dto.OrderItemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface OrderMapper {

    int insertOrder(OrderDTO newOrder);

    int insertOrderItems(List<OrderItemDTO> orderItems);

    Optional<OrderDTO> findByOrder(UUID orderId);

}
