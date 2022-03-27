package com.raysofthesun.poswebjava.customer.feign.application;

import com.raysofthesun.poswebjava.FeignConfig;
import com.raysofthesun.poswebjava.PosWebJavaApplication;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.stereotype.Component;
import reactivefeign.client.ReactiveHttpRequest;
import reactivefeign.client.ReactiveHttpRequestInterceptors;
import reactivefeign.webclient.WebReactiveFeign;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

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
                .contract(new SpringMvcContract(Collections.emptyList(), new DefaultFormattingConversionService()))
                .target(ApplyApplicationsApi.class, feignConfig.getApplyServiceBaseUrl());

    }
}
