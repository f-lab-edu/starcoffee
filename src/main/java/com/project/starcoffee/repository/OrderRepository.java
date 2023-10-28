package com.project.starcoffee.repository;

import com.project.starcoffee.domain.order.OrderStatus;
import com.project.starcoffee.dto.ItemDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository {
    int saveOrder(UUID memberId, List<ItemDTO> items, long storeId, int itemCount, int finalPrice);

}
