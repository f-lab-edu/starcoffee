package com.project.starcoffee.dao;

import com.project.starcoffee.dto.ItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
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
    public UUID saveItem(List<ItemDTO> itemDTO) {
        // cartId를 생성하는 더 좋은 방법이 없을까 ?
        UUID cartId = UUID.randomUUID();
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
    public void autoDeleteItem(UUID cartId) {
        List<ItemDTO> itemDTOS = cart.get(cartId);

        itemDTOS.stream().filter(item ->
                        ChronoUnit.SECONDS.between
                                ((Temporal) item.getCreatedAt(), (Temporal) Timestamp.valueOf(LocalDateTime.now())) > expireTime)
                        .findFirst().ifPresent(e -> {
                    log.info("장바구니에 담긴 상품이 일주일이 경과되어 삭제처리되었습니다.");
                    cart.remove(cartId);
                });
    }
}
