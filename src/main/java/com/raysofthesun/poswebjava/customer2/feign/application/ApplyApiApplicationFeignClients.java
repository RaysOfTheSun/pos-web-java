package com.raysofthesun.poswebjava.customer2.feign.application;

import com.raysofthesun.poswebjava.FeignConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactivefeign.webclient.WebReactiveFeign;

@Component
public class ApplyApiApplicationFeignClients {
    private final FeignConfig feignConfig;

    public ApplyApiApplicationFeignClients(FeignConfig feignConfig) {
        this.feignConfig = feignConfig;
    }

    @Bean(name = "customerApplyApplicationApi")
    ApplyApplicationsApi applyApplicationsApi() {
        return WebReactiveFeign
                .<ApplyApplicationsApi>builder()
                .target(ApplyApplicationsApi.class, feignConfig.getApplyServiceBaseUrl());

    }
}
