package com.project.starcoffee.repository;

import com.project.starcoffee.dto.MemberCardDTO;
import com.project.starcoffee.dto.OrderDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository {
    int saveOrder(UUID memberId,
                  UUID cartId,
                  Long storeId,
                  int totalItemCount,
                  int totalFinalPrice);

    OrderDTO findByOrder(UUID cartId);


}
