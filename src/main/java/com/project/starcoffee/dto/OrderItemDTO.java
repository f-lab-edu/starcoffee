package com.project.starcoffee.dto;

import com.project.starcoffee.domain.item.ItemType;
import com.project.starcoffee.domain.order.CupSize;
import com.project.starcoffee.domain.order.ItemSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private UUID orderId;
    @NotNull
    private Long itemId;
    @NotNull
    private String itemName;
    @NotNull
    private int itemPrice;
    @NotNull
    private ItemType itemType;
    @NotNull
    private ItemSize itemSize;
    @NotNull
    private CupSize cupSize;

}
