package com.project.starcoffee.domain.item;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class Item {
    private long itemId;
    private int nutritionId;
    private int itemCategoryId;
    private String itemName;
    private String itemEngName;
    private String itemDesc;
    private String itemImage;
    private int itemPrice;
    private ItemType itemType;
    private String allergy;

}
