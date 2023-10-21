package com.project.starcoffee.domain.order;

import lombok.*;

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
    private long orderPrice;
    private OrderStatus status;
    private ItemSize itemSize;
    private CupSize cup;
    private int itemCount;
    private long finalPrice;
}
