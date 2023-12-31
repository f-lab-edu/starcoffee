package com.project.starcoffee.config;

import com.project.starcoffee.config.argumentResolver.CardArgumentResolver;
import com.project.starcoffee.config.login.LoginCheckInterceptor;
import com.project.starcoffee.config.argumentResolver.LoginProcessArgumentResolver;
import com.project.starcoffee.repository.MemberRepository;
import com.project.starcoffee.service.LogCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;
    private final LogCardService logCardService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/",
                        "/members", "/members/home", "/members/login", "/cards/**", "/store/**",
                        "/error", "/swagger-ui/**", "/api-docs/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginProcessArgumentResolver(memberRepository));
        resolvers.add(new CardArgumentResolver(logCardService));
    }
}
