package com.project.starcoffee;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAsync				// 비동기 방식 활성화
@EnableScheduling			// 스케줄링 활성화
@EnableAspectJAutoProxy		// 최상위 클래스에서 적용해야 AOP 적용
//@EnableRedisHttpSession     // 레디스 세션 추가
@SpringBootApplication(scanBasePackages = "com.project.starcoffee")
public class StarcoffeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarcoffeeApplication.class, args);
	}

}
