package com.project.starcoffee.controller.request.store;

import com.project.starcoffee.domain.store.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class StoreRequest {

    private long storeId;
    @NotNull
    private String storeName;
    @NotNull
    private String storeImage;
    @NotNull
    private String storeAddress;
    @NotNull
    private String direction;
    @NotNull
    private String operationTime;

    private StoreStatus status;

}
