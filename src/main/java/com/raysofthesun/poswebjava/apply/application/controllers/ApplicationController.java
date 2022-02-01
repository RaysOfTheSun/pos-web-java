package com.raysofthesun.poswebjava.apply.application.controllers;

import com.raysofthesun.poswebjava.apply.application.models.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.application.models.Application;
import com.raysofthesun.poswebjava.apply.application.models.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.application.services.ApplicationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/apply/")
public class ApplicationController {
    protected ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/agents/{agentId}/applications/create")
    public Mono<Application> createApplicationFromProposal(@PathVariable String agentId,
                                                           @RequestBody ApplicationCreationRequest request) {
        return applicationService.createApplicationWithRequestAndAgentId(request, agentId);
    }

    @GetMapping("/applications/{applicationId}")
    public Mono<Application> getApplicationById(@PathVariable String applicationId) {
        return applicationService.getApplicationWithId(applicationId);
    }

//    @GetMapping("/customers/{customerId}/applications")
//    public Flux<Application> getAllApplicationsWithCustomerId(@PathVariable String customerId) {
//        return applicationService.getAllApplicationsWithCustomerId(customerId);
//    }

    @GetMapping("/customers/{customerId}/application-metas")
    public Flux<ApplicationMeta> getApplicationMetasWithCustomerId(@PathVariable String customerId,
                                                                   @RequestParam() int index,
                                                                   @RequestParam(defaultValue = "20") int pageSize) {
        return applicationService.getApplicationMetasWithCustomerId(customerId, index, pageSize);
    }

    @PostMapping("/applications/{applicationId}")
    public Mono<Boolean> updateApplicationWithId(@PathVariable String applicationId,
                                                 @RequestBody Application application) {
        return applicationService.updateApplication(applicationId, application);
    }

    @GetMapping("/customers/{customerId}/application-count")
    public Mono<Integer> getTotalApplicationCountForCustomerById(@PathVariable String customerId) {
        return applicationService.getTotalApplicationCountForCustomerById(customerId);
    }
}
