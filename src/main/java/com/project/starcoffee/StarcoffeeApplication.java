package com.project.starcoffee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = "com.project.starcoffee")
public class StarcoffeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarcoffeeApplication.class, args);
	}

}
