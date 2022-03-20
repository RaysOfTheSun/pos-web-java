package com.raysofthesun.poswebjava.apply2.applications.controllers;

import com.raysofthesun.poswebjava.apply2.applications.factories.ApplicationServiceFactory;
import com.raysofthesun.poswebjava.apply2.applications.models.core.application.Application;
import com.raysofthesun.poswebjava.apply2.applications.models.core.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply2.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.core.enums.Market;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "Apply Application Processes")
@RestController
@RequestMapping("v1/apply/applications")
public class ApplicationsController {

    private final ApplicationServiceFactory serviceFactory;

    public ApplicationsController(ApplicationServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @ApiOperation(value = "Create an Application", notes = "Create an application from a proposal")
    @PostMapping("/agents/{agentId}/applications/create")
    public Mono<Application> createApplicationWithRequest(@RequestBody ApplicationCreationRequest request,
                                                          @PathVariable String agentId) {
        return this.serviceFactory
                .getServiceForMarket(Market.COR)
                .createApplicationFromRequestAndAgentId(request, agentId);
    }

    @ApiOperation(value = "Get Application by ID")
    @GetMapping("/applications/{applicationId}")
    public Mono<Application> getApplicationById(@PathVariable String applicationId) {
        return this.serviceFactory
                .getServiceForMarket(Market.COR)
                .getApplicationById(applicationId);
    }

    @ApiOperation(
            value = "Get application metas for a given customer",
            notes = "Returns pageSize Application metas per page index")
    @GetMapping("/customers/{customerId}/application-metas")
    public Flux<ApplicationMeta> getApplicationMetasWithCustomerId(@PathVariable String customerId,
                                                                   @RequestParam() int index,
                                                                   @RequestParam(defaultValue = "20") int pageSize) {
        return this.serviceFactory
                .getServiceForMarket(Market.COR)
                .getApplicationMetasByCustomerId(customerId, index, pageSize);
    }

    @ApiOperation(
            value = "Get Application Count For Customer",
            produces = MediaType.TEXT_HTML_VALUE)
    @GetMapping("/customers/{customerId}/application-count")
    public Mono<Integer> getTotalApplicationCountForCustomerById(
            @ApiParam(value = "The id of the target customer", required = true) @PathVariable String customerId) {
        return this.serviceFactory
                .getServiceForMarket(Market.COR)
                .getTotalApplicationCountForCustomerById(customerId);
    }
}
