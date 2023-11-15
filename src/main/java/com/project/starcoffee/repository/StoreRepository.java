package com.project.starcoffee.repository;

import com.project.starcoffee.controller.request.store.StoreRequest;
import com.project.starcoffee.domain.store.Store;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository {

    int saveStore(StoreRequest storeRequest);
    int storeNotOpen(long storeId);
    Optional<Store> findById(long storeId);
    int updateStoreStatus(Store store);

}
