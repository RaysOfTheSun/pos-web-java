package com.raysofthesun.poswebjava.apply.feign;

import com.raysofthesun.poswebjava.FeignConfig;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.stereotype.Component;
import reactivefeign.webclient.WebReactiveFeign;

import java.util.Collections;

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
                .contract(new SpringMvcContract(Collections.emptyList(), new DefaultFormattingConversionService()))
                .target(CustomerApi.class, feignConfig.getAgentServiceBaseUrl());
    }
}
