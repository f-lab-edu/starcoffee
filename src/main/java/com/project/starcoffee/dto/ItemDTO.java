package com.project.starcoffee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.starcoffee.domain.item.ItemType;
import com.project.starcoffee.domain.order.CupSize;
import com.project.starcoffee.domain.order.ItemSize;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO implements Serializable {

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
    private CupSize cupSize;
    @NotNull
    private int itemCount;
    @NotNull
    private int finalPrice;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Timestamp createdAt;

}
