package com.project.starcoffee.controller;

import com.project.starcoffee.domain.store.Store;
import com.project.starcoffee.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PatchMapping("/open/{id}")
    public Store openStore(@PathVariable("id") long storeId) {
        storeService.openStore(storeId);
        return storeService.getStoreInfo(storeId);
    }

    @PatchMapping("/close/{id}")
    public Store closeStore(@PathVariable("id") long storeId) {
        storeService.closeStore(storeId);
        return storeService.getStoreInfo(storeId);
    }

}
