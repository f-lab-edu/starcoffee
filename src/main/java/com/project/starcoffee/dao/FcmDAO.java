package com.project.starcoffee.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.Message;
import com.project.starcoffee.utils.RedisKeyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FcmDAO implements FcmDAORepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public FcmDAO(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Value("${expire.fcm.member}")
    private long memberTokenExpireSecond;

    @Value("${expire.fcm.store}")
    private long storeTokenExpireSecond;


    /**
     * 고객이 발급받은 토큰을 저장한다.
     *
     * @param token 토큰 정보
     * @param memberId 고객의 고유 아이디
     */
    public void addMemberToken(String token, String memberId) {
        String key = RedisKeyFactory.generateFcmMemberKey(memberId);

        // 낙관적 락 (Optimistic Lock)
        redisTemplate.watch(key);

        try {
            if (getMemberTokens(memberId) != null) {
                log.info("이미 토큰이 존재합니다.");
                return;
            }
            redisTemplate.multi();  // 트랜잭션 시작
            redisTemplate.opsForValue().set(key, token);  // token 을 저장한다.
            // 키의 만료시간 설정 -> 설정한 시간이 지나면 Redis 는 해당 키에 대한 데이터를 자동으로 삭제
            redisTemplate.expire(key, memberTokenExpireSecond, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis Add Member Token Error! Key : {}", key);
            log.error("Error Info : {}", e.getMessage());
            redisTemplate.discard();    // 트랜잭션 거부
            throw new RuntimeException("고객의 토큰정보를 저장할 수 없습니다.");
        }
    }

    /**
     * 가게가 발급받은 토큰을 저장한다.
     *
     * @param token 토큰정보
     * @param storeId 가게의 고유 아이디
     */
    public void addStoreToken(long storeId, String token) {
        String key = RedisKeyFactory.generateFcmStoreKey(storeId);

        redisTemplate.watch(key);

        try {
            if (getStoreTokens(storeId) != null) {
                log.info("이미 토큰이 존재합니다.");
                return;
            }
            redisTemplate.multi();
            redisTemplate.opsForValue().set(key, token);
            redisTemplate.expire(key, memberTokenExpireSecond, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis Add Store Token Error! Key : {}", key);
            log.error("ERROR Info : {}", e.getMessage());
            redisTemplate.discard();
            throw new RuntimeException("가게의 토큰정보를 저장할 수 없습니다.");
        }
    }

    /**
     * 해당 고객의 토큰 리스트를 조회한다.
     *
     * @param memberId 고객의 고유 아이디
     * @return
     */
    public String getMemberTokens(String memberId) {
        String token = redisTemplate.opsForValue().get(RedisKeyFactory.generateFcmMemberKey(memberId));
        return token;
    }


    /**
     * 해당 가게의 토큰 리스트를 조회한다.
     *
     * @param storeId 가게의 고유 아이디
     * @return
     */
    public String getStoreTokens(long storeId) {
        String token = redisTemplate.opsForValue().get(RedisKeyFactory.generateFcmStoreKey(storeId));
        return token;
    }

    public void deleteToken(String memberId) {
        String key = RedisKeyFactory.generateFcmMemberKey(memberId);
        redisTemplate.delete(key);
    }

    public void addMemberErrorPush(String memberId, Message message) {
        redisTemplate.opsForValue().set(RedisKeyFactory.generateFcmMemberErrorKey(memberId), message.toString());
    }

    public void addStoreErrorPush(long storeId, Message message) {
        redisTemplate.opsForValue().set(RedisKeyFactory.generateFcmStoreErrorKey(storeId), message.toString());
    }


}
