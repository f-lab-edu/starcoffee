package com.project.starcoffee.dto;

import com.project.starcoffee.domain.item.ItemType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class ItemDTO {
    @NotNull
    private long itemId;
    @NotNull
    private String itemName;
    @NotNull
    private int itemPrice;
    @NotNull
    private ItemType itemType;
}
