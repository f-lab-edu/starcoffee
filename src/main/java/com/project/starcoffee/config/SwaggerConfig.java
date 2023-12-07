package com.project.starcoffee.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Info starCoffeeProject = new Info().title("StarCoffee Project").version(appVersion)
                .description("스타벅스App을 벤치마킹한 스타커피 Back-End Project")
                .contact(new Contact().name("Project-Github").url("https://github.com/f-lab-edu/starcoffee"));

        return new OpenAPI()
                .components(new Components())
                .info(starCoffeeProject);
    }


}
