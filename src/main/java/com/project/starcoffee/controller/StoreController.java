package com.project.starcoffee.controller;

import com.project.starcoffee.controller.response.store.StoreStatusResponse;
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
    public StoreStatusResponse openStore(@PathVariable("id") long storeId) {
        return storeService.openStore(storeId);
    }

    @PatchMapping("/close/{id}")
    public StoreStatusResponse closeStore(@PathVariable("id") long storeId) {
        return storeService.closeStore(storeId);
    }

}
