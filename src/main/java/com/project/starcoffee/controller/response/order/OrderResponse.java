package com.project.starcoffee.controller.response.order;

import com.project.starcoffee.dto.OrderDTO;
import com.project.starcoffee.dto.OrderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private UUID orderId;
    private UUID memberId;
    private long storeId;
    private int itemCount;
    private long finalPrice;
    private List<OrderItemDTO> orderItems;


}
