package com.project.starcoffee.service;

import com.project.starcoffee.domain.store.Store;
import com.project.starcoffee.exception.CanNotOpenShopException;
import com.project.starcoffee.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public void openStore(long storeId) {
        // 매장이 오픈 중일 때
        if (isClose(storeId) == false) {
            throw new CanNotOpenShopException("영업이 이미 진행중입니다.");
        }
        int result = storeRepository.updateStoreOpenById(storeId);
        if (result != 1) {
            log.error("open store ERROR! storeId : {}", storeId);
            throw new RuntimeException("Open Store ERROR!");
        }
    }

    private boolean isClose(long storeId) {
        long closeStoreResult = storeRepository.storeNotOpen(storeId);
        return closeStoreResult == 1;
    }

    public Store getStoreInfo(long storeId) {
        return storeRepository.findById(storeId);
    }

    public void closeStore(long storeId) {
        // 매장이 오픈중이 아닐 때
        if (isClose(storeId) == true) {
            throw new CanNotOpenShopException("이미 영업을 종료한 매장입니다.");
        }

        int result = storeRepository.updateStoreCloseById(storeId);
        if (result != 1) {
            log.error("close Store ERROR! storeId : {}", storeId);
        }

    }
}
