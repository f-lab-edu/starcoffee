package com.project.starcoffee.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.starcoffee.dto.ItemDTO;
import com.project.starcoffee.utils.RedisKeyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class CartRedisDAO implements CartDAORedisRepository {
    private final RedisTemplate<String, List<ItemDTO>> redisListTemplate;

    @Value("${expire.cart}")
    private long cartExpireSecond;

    @Autowired
    public CartRedisDAO(RedisTemplate<String, List<ItemDTO>> redisListTemplate) {
        this.redisListTemplate = redisListTemplate;
    }

    public void addCart(UUID cartId, List<ItemDTO> itemDTO) {
        String key = RedisKeyFactory.generateCartKey(cartId);
        redisListTemplate.watch(key);

        try {
            if (duplicated(cartId)) {
                log.info("이미 장바구니가 존재합니다.");
                return;
            }

            redisListTemplate.multi();
            redisListTemplate.opsForValue().set(key, itemDTO);
            redisListTemplate.expire(key, cartExpireSecond, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis Add Cart Error! Key : {}", key);
            log.error("Error Info : {}, ", e.getMessage());
            redisListTemplate.discard();
            throw new RuntimeException("고객의 장바구니를 저장할 수 없습니다.");
        }
    }

    public boolean duplicated(UUID cartId) {
        return redisListTemplate.hasKey(RedisKeyFactory.generateCartKey(cartId));
    }

    public List<ItemDTO> getCartItems(UUID cartId) {
        return redisListTemplate.opsForValue().get(RedisKeyFactory.generateCartKey(cartId));
    }


    public void deleteCartItems(UUID cartId) {
        redisListTemplate.delete(RedisKeyFactory.generateCartKey(cartId));
    }

}
