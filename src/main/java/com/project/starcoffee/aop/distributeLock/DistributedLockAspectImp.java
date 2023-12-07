package com.project.starcoffee.aop.distributeLock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspectImp {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";
    private final RedissonClient redissonClient;
    private final DistributionTransaction newTransaction;

    @Around("@annotation(com.project.starcoffee.aop.distributeLock.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        // 키 생성
        String key = REDISSON_LOCK_PREFIX + CustomParser.createKey(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());

        // 락 획득
        RLock lock = redissonClient.getLock(key);

        try {
            boolean isAvailable = lock.tryLock
                    (distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!isAvailable) {
                return false;
            }

            log.info("Redisson Lock Key : {}", key);

            // 새로운 트랜잭션 생성(Propagation.REQUIRES_NEW) 후, 기존 로직 실행
            return newTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                // 락 해제
                lock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock {} {}",
                        kv("serviceName", method.getName()),
                        kv("key", key));
            }
        }


    }

    public static String kv(String key, Object value) {
        return String.format("%s=%s", key, value);
    }
}
