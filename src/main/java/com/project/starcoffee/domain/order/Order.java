package com.project.starcoffee.domain.order;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class Order {
    private UUID orderId;
    private UUID memberId;
    private long itemId;
    private long storeId;
    private OrderStatus status;
    private int itemCount;
    private long finalPrice;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
