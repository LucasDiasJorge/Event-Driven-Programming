package com.service.sse.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class FeignRequestInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate template) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Jwt jwt = (Jwt) securityContext.getAuthentication().getPrincipal();
        template.header("Authorization", "Bearer " + jwt.getTokenValue());
        return;

    }

}
