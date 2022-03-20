package com.raysofthesun.poswebjava.apply.feign;

import com.raysofthesun.poswebjava.FeignConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactivefeign.webclient.WebReactiveFeign;

@Component
public class CustomerApiFeignClients {

    private final FeignConfig feignConfig;

    public CustomerApiFeignClients(FeignConfig feignConfig) {
        this.feignConfig = feignConfig;
    }

    @Bean
    public CustomerApi customerApi() {
        return WebReactiveFeign
                .<CustomerApi>builder()
                .target(CustomerApi.class, feignConfig.getAgentServiceBaseUrl());
    }
}
