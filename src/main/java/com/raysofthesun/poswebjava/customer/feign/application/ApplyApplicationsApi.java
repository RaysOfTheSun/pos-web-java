package com.raysofthesun.poswebjava.customer.feign.application;


import com.raysofthesun.poswebjava.core.enums.Market;
import com.raysofthesun.poswebjava.customer.feign.application.models.ApiApplicationMeta;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplyApplicationsApi {

    @GetMapping(path = "/{market}/customers/{customerId}/application-metas")
    Flux<ApiApplicationMeta> getApplicationsByCustomerId(
            @PathVariable("market") Market market,
            @PathVariable("customerId") String customerId,
            @RequestParam("index") int index
    );

    @GetMapping("{market}/customers/{customerId}/application-count")
    Mono<Integer> getTotalApplicationCountForCustomerById(
            @PathVariable("market") Market market,
            @PathVariable("customerId") String customerId
    );
}
