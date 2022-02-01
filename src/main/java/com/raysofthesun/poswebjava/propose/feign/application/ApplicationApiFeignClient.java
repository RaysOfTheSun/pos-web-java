package com.raysofthesun.poswebjava.propose.feign.application;

import com.raysofthesun.poswebjava.FeignConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactivefeign.webclient.WebReactiveFeign;

@Component
public class ApplicationApiFeignClient {

    private final FeignConfig feignConfig;

    public ApplicationApiFeignClient(FeignConfig feignConfig) {
        this.feignConfig = feignConfig;
    }

    @Bean
    public ApplyApplicationApi applyApplicationApi() {
        return WebReactiveFeign
                .<ApplyApplicationApi>builder()
                .target(ApplyApplicationApi.class, feignConfig.getApplyServiceBaseUrl());
    }
}