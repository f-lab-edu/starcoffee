package com.project.starcoffee.controller;

import com.project.starcoffee.controller.request.store.StoreRequest;
import com.project.starcoffee.controller.response.store.StoreStatusResponse;
import com.project.starcoffee.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;
    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping()
    public void signUp(@RequestBody StoreRequest storeRequest) {
        storeService.saveStore(storeRequest);
    }

    @PatchMapping("/open/{id}")
    public StoreStatusResponse openStore(@PathVariable("id") long storeId) {
        return storeService.openStore(storeId);
    }

    @PatchMapping("/close/{id}")
    public StoreStatusResponse closeStore(@PathVariable("id") long storeId) {
        return storeService.closeStore(storeId);
    }

    @GetMapping("/status")
    public String StoreStatus(@RequestParam long storeId) {
        return storeService.getStoreStatus(storeId);
    }

}
