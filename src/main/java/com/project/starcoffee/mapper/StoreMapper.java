package com.project.starcoffee.mapper;

import com.project.starcoffee.controller.request.store.StoreRequest;
import com.project.starcoffee.domain.store.Store;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface StoreMapper {
    int saveStore(StoreRequest storeRequest);
    int storeNotOpen(long storeId);
    Optional<Store> findById(long storeId);
    int updateStoreStatus(Store store);
}
