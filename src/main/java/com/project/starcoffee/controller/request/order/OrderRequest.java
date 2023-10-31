package com.project.starcoffee.controller.request.order;

import com.project.starcoffee.domain.item.ItemType;
import com.project.starcoffee.domain.order.CupSize;
import com.project.starcoffee.domain.order.ItemSize;
import com.project.starcoffee.dto.ItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    @NotNull
    private long itemId;
    @NotNull
    private String itemName;
    @NotNull
    private int itemPrice;
    @NotNull
    private ItemType itemType;
    @NotNull
    private ItemSize itemSize;
    @NotNull
    private CupSize itemCup;
    @NotNull
    private int itemCount;
    @NotNull
    private int finalPrice;
    @NotNull
    private Timestamp createdAt;

}
