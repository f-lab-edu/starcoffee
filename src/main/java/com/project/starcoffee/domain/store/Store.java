package com.project.starcoffee.domain.store;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class Store {
    private long storeId;
    private String storeName;
    private String storeImage;
    private String storeAddress;
    private String direction;
    private String operationTime;
    private StoreStatus status;
}
