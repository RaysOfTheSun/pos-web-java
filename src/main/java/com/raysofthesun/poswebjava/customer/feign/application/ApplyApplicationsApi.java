package com.raysofthesun.poswebjava.customer.feign.application;


import com.raysofthesun.poswebjava.customer.feign.application.models.ApiApplicationMeta;
import feign.Param;
import feign.RequestLine;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplyApplicationsApi {

    @RequestLine("GET /customers/{customerId}/application-metas")
    Flux<ApiApplicationMeta> getApplicationsByCustomerId(@Param("customerId") String customerId,
                                                         @Param("index") int index);

    @RequestLine("GET /customers/{customerId}/application-count")
    Mono<Integer> getTotalApplicationCountForCustomerById(@Param("customerId") String customerId);
}
