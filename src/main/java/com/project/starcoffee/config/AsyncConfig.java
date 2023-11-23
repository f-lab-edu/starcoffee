package com.project.starcoffee.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean("Async-Executor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);      // 기본 생성 스레드 갯수
        threadPoolTaskExecutor.setMaxPoolSize(10);      // 최대 스레드 갯수
        threadPoolTaskExecutor.setQueueCapacity(20);    // 대기 스레드 최대 갯수
        threadPoolTaskExecutor.setThreadNamePrefix("Async-Executor");   // 스레드 이름 설정
        // 스레드 풀이 최대일 때, 작업이 추가되지 않도록 하는 정책
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        return threadPoolTaskExecutor;
    }

}
