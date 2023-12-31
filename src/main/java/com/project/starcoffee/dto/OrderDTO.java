package com.project.starcoffee.dto;

import com.project.starcoffee.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private UUID orderId;
    @NotNull
    private UUID memberId;
    @NotNull
    private long storeId;
    @NotNull
    private OrderStatus status;
    @NotNull
    private int itemCount;
    @NotNull
    private long finalPrice;
    @NotNull
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<OrderItemDTO> orderItemList;

}
