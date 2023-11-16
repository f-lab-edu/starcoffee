package com.project.starcoffee.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.Message;
import com.project.starcoffee.aop.SessionMemberId;
import com.project.starcoffee.utils.RedisKeyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
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
    public void addMemberToken(String token, String memberId) {
        String key = RedisKeyFactory.generateFcmMemberKey(memberId);

        if (getMemberTokens(memberId).contains(token)) {
            log.info("이미 토큰이 존재합니다.");
            return;
        }
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                // 낙관적 락 (Optimistic Lock)
                redisTemplate.watch(key);
                try {
                    redisTemplate.multi();                                  // 트랜잭션 시작
                    redisTemplate.opsForList().rightPush(key, token);       // List 형태로 오른쪽 추가
                    // 키의 만료시간 설정 -> 설정한 시간이 지나면 Redis 는 해당 키에 대한 데이터를 자동으로 삭제
                    redisTemplate.expire(key, memberTokenExpireSecond, TimeUnit.SECONDS);
                } catch (Exception e) {
                    log.error("Redis Add Member Token Error! Key : {}", key);
                    log.error("Error Info : {}", e.getMessage());
                    redisTemplate.discard();    // 트랜잭션 거부
                    throw new RuntimeException("고객의 토큰정보를 저장할 수 없습니다.");
                }

                return redisTemplate.exec(); // 트랜잭션 실행
            }
        });


    }

    /**
     * 가게가 발급받은 토큰을 저장한다.
     *
     * @param token 토큰정보
     * @param storeId 가게의 고유 아이디
     */
    public void addStoreToken(String token, long storeId) {
        String key = RedisKeyFactory.generateFcmStoreKey(storeId);

        if (getStoreTokens(storeId).contains(key)) {
            log.info("이미 토큰이 존재합니다.");
            return;
        }

        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                redisTemplate.watch(key);
                try {
                    redisTemplate.multi();
                    redisTemplate.opsForList().rightPush(key, token);
                    // 가게 토큰은 유효하다고 가정한다.
                } catch (Exception e) {
                    log.error("Redis Add Store Token Error! Key : {}", key);
                    log.error("ERROR Info : {}", e.getMessage());
                    redisTemplate.discard();
                    throw new RuntimeException("가게의 토큰정보를 저장할 수 없습니다.");
                }
                return redisTemplate.exec();
            }
        });
    }


    /**
     * 해당 고객의 토큰 리스트를 조회한다.
     *
     * @param memberId 고객의 고유 아이디
     * @return
     */
    public List<String> getMemberTokens(String memberId) {
        List<String> resultList = redisTemplate.opsForList().range(RedisKeyFactory.generateFcmMemberKey(memberId), 0, -1)
                .stream()
                .map(e -> objectMapper.convertValue(e, String.class))
                .collect(Collectors.toList());

        if (resultList.isEmpty()) {
            throw new RuntimeException("해당 멤버의 토큰이 존재하지 않습니다.");
        }

        return resultList;
    }


    /**
     * 해당 가게의 토큰 리스트를 조회한다.
     *
     * @param storeId 가게의 고유 아이디
     * @return
     */
    public List<String> getStoreTokens(long storeId) {
        List<String> resultList = redisTemplate.opsForList().range(RedisKeyFactory.generateFcmStoreKey(storeId), 0, -1)
                .stream()
                .map(e -> objectMapper.convertValue(e, String.class))
                .collect(Collectors.toList());

        if (resultList.isEmpty()) {
            throw new RuntimeException("해당 가게의 토큰이 존재하지 않습니다.");
        }

        return resultList;
    }

    public void deleteToken(String memberId) {
        String key = RedisKeyFactory.generateFcmMemberKey(memberId);
        redisTemplate.delete(key);
    }

    public void addMemberErrorPush(String memberId, List<Message> messages) {
        redisTemplate.opsForList().rightPush(RedisKeyFactory.generateFcmMemberErrorKey(memberId), messages);
    }

    public void addStoreErrorPush(long storeId, List<Message> messages) {
        redisTemplate.opsForList().rightPush(RedisKeyFactory.generateFcmStoreErrorKey(storeId), messages);
    }


}
