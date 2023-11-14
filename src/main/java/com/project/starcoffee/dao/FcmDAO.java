package com.project.starcoffee.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.Message;
import com.project.starcoffee.aop.SessionMemberId;
import com.project.starcoffee.utils.RedisKeyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FcmDAO implements FcmDAORepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public FcmDAO(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
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
    @SessionMemberId
    public void addMemberToken(String token, String memberId) {
        String key = RedisKeyFactory.generateFcmMemberKey(memberId);

        // 낙관적 락 (Optimistic Lock)
        redisTemplate.watch(key);

        try {
            if (getMemberTokens(memberId).contains(token)) {
                return;
            }

            redisTemplate.multi();  // 트랜잭션 시작
            redisTemplate.opsForList().rightPush(key, token); // List 형태로 오른쪽 추가
            // 키의 만료시간 설정 -> 설정한 시간이 지나면 Redis 는 해당 키에 대한 데이터를 자동으로 삭제
            redisTemplate.expire(key, memberTokenExpireSecond, TimeUnit.SECONDS);

            redisTemplate.exec();   // 트랜잭션 처리
        } catch (Exception e) {
            log.error("Redis Add Member Token Error! Key : {}", key);
            log.error("Error Info : {}", e.getMessage());
            redisTemplate.discard();    // 트랜잭션 거부
            throw new RuntimeException("고객의 토큰정보를 저장할 수 없습니다.");
        }
    }

    public void addStoreToken(String token, String storeId) {
        String key = RedisKeyFactory.generateFcmStoreKey(storeId);
        redisTemplate.watch(key);

        try {
            if (getStoreTokens(storeId).contains(key)) {
                return;
            }
            redisTemplate.multi();
            redisTemplate.opsForList().rightPush(key, token);
            redisTemplate.expire(key, storeTokenExpireSecond, TimeUnit.SECONDS);

            redisTemplate.exec();
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
    public List<String> getMemberTokens(String memberId) {
        return redisTemplate.opsForList().range(RedisKeyFactory.generateFcmMemberKey(memberId), 0, -1)
                .stream()
                .map(e -> objectMapper.convertValue(e, String.class))
                .collect(Collectors.toList());
    }


    /**
     * 해당 가게의 토큰 리스트를 조회한다.
     *
     * @param storeId 가게의 고유 아이디
     * @return
     */
    public List<String> getStoreTokens(String storeId) {
        List<String> tokenList = redisTemplate.opsForList().range(RedisKeyFactory.generateFcmStoreKey(storeId), 0, -1)
                .stream()
                .map(e -> objectMapper.convertValue(e, String.class))
                .collect(Collectors.toList());

        if (tokenList.isEmpty()) {
            throw new RuntimeException("해당 가게의 토큰이 존재하지 않습니다.");
        }

        return tokenList;
    }

    public void addMemberErrorPush(String memberId, List<Message> messages) {
        redisTemplate.opsForList().rightPush(RedisKeyFactory.generateFcmMemberErrorKey(memberId), messages);
    }

    public void addStoreErrorPush(String storeId, List<Message> messages) {
        redisTemplate.opsForList().rightPush(RedisKeyFactory.generateFcmStoreErrorKey(storeId), messages);
    }


}
