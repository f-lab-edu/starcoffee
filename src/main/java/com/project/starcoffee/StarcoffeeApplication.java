package com.project.starcoffee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StarcoffeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarcoffeeApplication.class, args);
	}

}
