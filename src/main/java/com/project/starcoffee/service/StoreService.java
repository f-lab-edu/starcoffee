package com.project.starcoffee.service;

import com.project.starcoffee.controller.response.store.StoreStatusResponse;
import com.project.starcoffee.domain.store.Store;
import com.project.starcoffee.domain.store.StoreStatus;
import com.project.starcoffee.exception.CanNotOpenShopException;
import com.project.starcoffee.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }


    public Store getStoreInfo(long storeId) {
        Optional<Store> storeOptional = storeRepository.findById(storeId);
        storeOptional.orElseThrow(() -> new RuntimeException("가게 정보를 찾을 수 없습니다."));
        return storeOptional.get();
    }

    private boolean isClose(long storeId) {
        int closeStoreResult = storeRepository.storeNotOpen(storeId);
        return closeStoreResult == 1;
    }

    public StoreStatusResponse openStore(long storeId) {
        // 매장이 오픈일 때
        if (!isClose(storeId)) {
            throw new CanNotOpenShopException("영업이 이미 진행중입니다.");
        }

        Optional<Store> storeOptional = storeRepository.findById(storeId);

        storeOptional.ifPresent(storeEntity -> {
            storeEntity.setStatus(StoreStatus.OPEN);
            int result = storeRepository.updateStoreStatus(storeEntity);
            if (result != 1) {
                throw new RuntimeException("Open Store ERROR!");
            }
        });

        StoreStatusResponse storeStatusResponse = storeOptional.map(store -> {
            String name = store.getStoreName();
            StoreStatus status = store.getStatus();
            return new StoreStatusResponse(name, status);
        }).orElse(null);


        return storeStatusResponse;
    }

    public StoreStatusResponse closeStore(long storeId) {
        // 매장이 이미 닫았을 때
        if (isClose(storeId)) {
            throw new CanNotOpenShopException("이미 영업을 종료한 매장입니다.");
        }

        Optional<Store> storeOptional = storeRepository.findById(storeId);
        storeOptional.ifPresent(storeEntity -> {
            storeEntity.setStatus(StoreStatus.CLOSE);
            int result = storeRepository.updateStoreStatus(storeEntity);
            if (result != 1) {
                log.error("close Store ERROR! storeId : {}", storeId);
            }
        });

        StoreStatusResponse storeStatusResponse = storeOptional.map(store -> {
            String name = store.getStoreName();
            StoreStatus status = store.getStatus();
            return new StoreStatusResponse(name, status);
        }).orElse(null);

        return storeStatusResponse;

    }


}
