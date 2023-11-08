package com.project.starcoffee.repository;

import com.project.starcoffee.domain.store.Store;
import com.project.starcoffee.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StoreRepositoryImpl implements StoreRepository {

    private final StoreMapper storeMapper;

    @Autowired
    public StoreRepositoryImpl(StoreMapper storeMapper) {
        this.storeMapper = storeMapper;
    }

    @Override
    public int storeNotOpen(long storeId) {
        return storeMapper.storeNotOpen(storeId);
    }

    @Override
    public Optional<Store> findById(long storeId) {
        return storeMapper.findById(storeId);
    }

    @Override
    public int updateStoreStatus(Store store) {
        return storeMapper.updateStoreStatus(store);
    }
}
