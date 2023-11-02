package com.project.starcoffee;

import com.project.starcoffee.config.WebSessionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(WebSessionConfig.class)
public class StarcoffeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarcoffeeApplication.class, args);
	}

}
