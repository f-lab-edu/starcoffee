package com.project.starcoffee.controller.request.order;

import com.project.starcoffee.dto.ItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private List<ItemDTO> items;
    private long storeId;
    private String itemSize;
    private String itemCup;
    private int itemCount;
    private int itemPrice;
}
