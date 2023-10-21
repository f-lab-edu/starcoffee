package com.project.starcoffee.repository;

import com.project.starcoffee.domain.store.Store;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository {

    long storeNotOpen(Long storeId);
    int updateStoreOpenById(long storeId);
    int updateStoreCloseById(long storeId);
    Store findById(long storeId);


}
