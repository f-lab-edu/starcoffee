package com.project.starcoffee.domain.item;

import com.project.starcoffee.domain.order.CupSize;
import com.project.starcoffee.domain.order.ItemSize;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode
public class Item {
    private long itemId;
    private String itemName;
    private String itemEngName;
    private String itemDesc;
    private String itemImage;
    private int itemPrice;
    private ItemType itemType;
    private ItemSize itemSize;
    private CupSize itemCup;
    private String allergy;

}
