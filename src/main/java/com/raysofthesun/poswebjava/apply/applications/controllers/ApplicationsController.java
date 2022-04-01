package com.raysofthesun.poswebjava.apply.applications.controllers;

import com.raysofthesun.poswebjava.apply.applications.factories.ApplicationServiceFactory;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.Application;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.core.configuration.models.PosDocumentRequirement;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Api(tags = "Apply Application Processes")
@RestController
@RequestMapping("/v1/apply")
public class ApplicationsController {

    private final ApplicationServiceFactory serviceFactory;

    public ApplicationsController(ApplicationServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @ApiOperation(value = "Create an Application", notes = "Create an application from a proposal")
    @PostMapping("/{market}/agents/{agentId}/applications/create")
    public Mono<Application> createApplicationWithRequest(@RequestBody ApplicationCreationRequest request,
                                                          @PathVariable String agentId,
                                                          @PathVariable Market market) {
        return this.serviceFactory
                .getServiceForMarket(market)
                .createApplicationFromRequestAndAgentId(request, agentId, market);
    }

    @ApiOperation(value = "Get Application by ID")
    @GetMapping("/{market}/applications/{applicationId}")
    public Mono<Application> getApplicationById(@PathVariable String applicationId, @PathVariable Market market) {
        Logger.getAnonymousLogger().info(applicationId);
        return this.serviceFactory
                .getServiceForMarket(market)
                .getApplicationById(applicationId, market);
    }

    @ApiOperation(
            value = "Get application metas for a given customer",
            notes = "Returns pageSize Application metas per page index")
    @GetMapping("/{market}/customers/{customerId}/application-metas")
    public Flux<ApplicationMeta> getApplicationMetasWithCustomerId(@PathVariable String customerId,
                                                                   @PathVariable Market market,
                                                                   @RequestParam() int index,
                                                                   @RequestParam(defaultValue = "20") int pageSize) {
        return this.serviceFactory
                .getServiceForMarket(market)
                .getApplicationMetasByCustomerId(customerId, index, pageSize);
    }

    @ApiOperation(
            value = "Get Application Count For Customer",
            produces = MediaType.TEXT_HTML_VALUE)
    @GetMapping("/{market}/customers/{customerId}/application-count")
    public Mono<Integer> getTotalApplicationCountForCustomerById(
            @PathVariable Market market,
            @ApiParam(value = "The id of the target customer", required = true) @PathVariable String customerId) {
        return this.serviceFactory
                .getServiceForMarket(market)
                .getTotalApplicationCountForCustomerById(customerId);
    }

    @ApiOperation("Get the document requirements per insured")
    @GetMapping("/{market}/applications/{applicationId}/doc-reqs")
    public Mono<Map<String, List<PosDocumentRequirement>>> getDocumentReqsByApplicationId(@PathVariable String applicationId,
                                                                                                    @PathVariable Market market) {
        return this.serviceFactory
                .getServiceForMarket(market)
                .getDocumentRequirementsForInsuredById(applicationId, market);
    }
}
