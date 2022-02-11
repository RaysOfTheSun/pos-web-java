package com.raysofthesun.poswebjava.apply.application.controllers;

import com.raysofthesun.poswebjava.apply.application.models.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.application.models.Application;
import com.raysofthesun.poswebjava.apply.application.models.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.application.services.ApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "Apply Application Processes")
@RestController
@RequestMapping("v1/apply/")
public class ApplicationController {
    protected ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @ApiOperation(value = "Create an Application", notes = "Create an application from a proposal")
    @PostMapping("/agents/{agentId}/applications/create")
    public Mono<Application> createApplicationFromProposal(@PathVariable String agentId,
                                                           @RequestBody ApplicationCreationRequest request) {
        return applicationService.createApplicationWithRequestAndAgentId(request, agentId);
    }

    @ApiOperation(value = "Get Application by ID")
    @GetMapping("/applications/{applicationId}")
    public Mono<Application> getApplicationById(@PathVariable String applicationId) {
        return applicationService.getApplicationWithId(applicationId);
    }

//    @GetMapping("/customers/{customerId}/applications")
//    public Flux<Application> getAllApplicationsWithCustomerId(@PathVariable String customerId) {
//        return applicationService.getAllApplicationsWithCustomerId(customerId);
//    }

    @ApiOperation(
            value = "Get application metas for a given customer",
            notes = "Returns pageSize Application metas per page index")
    @GetMapping("/customers/{customerId}/application-metas")
    public Flux<ApplicationMeta> getApplicationMetasWithCustomerId(@PathVariable String customerId,
                                                                   @RequestParam() int index,
                                                                   @RequestParam(defaultValue = "20") int pageSize) {
        return applicationService.getApplicationMetasWithCustomerId(customerId, index, pageSize);
    }

    @ApiOperation("Update a given application by ID")
    @PostMapping("/applications/{applicationId}")
    public Mono<Boolean> updateApplicationWithId(@PathVariable String applicationId,
                                                 @RequestBody Application application) {
        return applicationService.updateApplication(applicationId, application);
    }

    @ApiOperation(
            value = "Get Application Count For Customer",
            produces = MediaType.TEXT_HTML_VALUE)
    @GetMapping("/customers/{customerId}/application-count")
    public Mono<Integer> getTotalApplicationCountForCustomerById(
            @ApiParam(value = "The id of the target customer", required = true) @PathVariable String customerId) {
        return applicationService.getTotalApplicationCountForCustomerById(customerId);
    }
}
