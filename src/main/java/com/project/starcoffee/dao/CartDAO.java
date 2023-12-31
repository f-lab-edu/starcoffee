package com.project.starcoffee.dao;

import com.project.starcoffee.dto.ItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class CartDAO implements CartDAORepository {
    private ConcurrentHashMap<UUID, List<ItemDTO>> cart;

    @Value("${expire.cart}")
    private Long expireTime;

    @PostConstruct
    public void init() {
        this.cart = new ConcurrentHashMap<>();
    }

    @Override
    public UUID saveItem(UUID cartId, List<ItemDTO> itemDTO) {
        cart.put(cartId, itemDTO);
        return cartId;
    }

    @Override
    public List<ItemDTO> findItem(UUID cartId) {
        return cart.get(cartId);
    }

    @Override
    public void deleteItem(UUID cartId) {
        cart.remove(cartId);
    }

    @Override
    public boolean duplicatedId(UUID cartId) {
        return cart.containsKey(cartId);
    }

    @Override
    public void autoDeleteItem() {
        LocalDateTime now = LocalDateTime.now();
        cart.values().forEach(itemList -> {
            itemList.removeIf(item ->
            {
                LocalDateTime createdAt = item.getCreatedAt().toLocalDateTime();
                Duration duration = Duration.between(createdAt, now);
                return duration.getSeconds() > expireTime;
            });
            log.info("장바구니에 담긴 상품이 일주일이 경과되어 삭제처리되었습니다.");
        });
    }
}
