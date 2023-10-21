package com.project.starcoffee.repository.mybatis;

import com.project.starcoffee.domain.store.Store;
import com.project.starcoffee.repository.StoreRepository;
import com.project.starcoffee.repository.mybatis.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StoreRepositoryImpl implements StoreRepository {

    private final StoreMapper storeMapper;

    @Autowired
    public StoreRepositoryImpl(StoreMapper storeMapper) {
        this.storeMapper = storeMapper;
    }

    @Override
    public long storeNotOpen(Long storeId) {
        return storeMapper.storeNotOpen(storeId);
    }

    @Override
    public int updateStoreOpenById(long storeId) {
        return storeMapper.updateStoreOpenById(storeId);
    }

    @Override
    public int updateStoreCloseById(long storeId) {
        return storeMapper.updateStoreCloseById(storeId);
    }

    @Override
    public Store findById(long storeId) {
        return storeMapper.findById(storeId);
    }
}
