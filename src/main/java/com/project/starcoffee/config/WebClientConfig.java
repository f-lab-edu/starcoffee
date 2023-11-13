package com.project.starcoffee.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpSession;

@Slf4j
@Configuration
public class WebClientConfig {

    // 세션 유틸에서도 비슷한 형태로 진행할 수 있지 않을까 ?
    @Bean
    public WebClient webClient(HttpSession session) {
        return WebClient.builder().baseUrl("http://localhost:8080")
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .filter(addSessionId(session))
                .build();
    }


    private ExchangeFilterFunction addSessionId(HttpSession session) {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            String sessionId = session.getId();
            return Mono.just(ClientRequest.from(clientRequest)
                    .header("Cookie", "JSESSIONID=" + sessionId)
                    .build());
        });
    }
}
