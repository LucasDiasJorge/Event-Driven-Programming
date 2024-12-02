package com.service.sse.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public FeignRequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor();
    }

}
