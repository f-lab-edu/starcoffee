package com.project.starcoffee.dto;

import com.project.starcoffee.domain.item.ItemType;
import com.project.starcoffee.domain.order.CupSize;
import com.project.starcoffee.domain.order.ItemSize;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    @NotNull
    private long storeId;
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
