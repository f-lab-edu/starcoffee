package com.project.starcoffee.repository.mybatis.mapper;

import com.project.starcoffee.domain.store.Store;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper {
    long storeNotOpen(Long storeId);
    int updateStoreOpenById(long storeId);
    int updateStoreCloseById(long storeId);
    Store findById(long storeId);
}
