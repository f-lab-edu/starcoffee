package com.project.starcoffee.controller.response.store;

import com.project.starcoffee.domain.store.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreStatusResponse {
    private String storeName;
    private StoreStatus status;
}
