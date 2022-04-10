package com.raysofthesun.poswebjava.customer.controllers;

import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.customer.feign.application.models.ApiApplicationMeta;
import com.raysofthesun.poswebjava.customer.models.Customer;
import com.raysofthesun.poswebjava.customer.models.CustomerSummary;
import com.raysofthesun.poswebjava.customer.models.RawCustomer;
import com.raysofthesun.poswebjava.customer.factories.CustomerServiceFactory;
import com.raysofthesun.poswebjava.customer.models.SuccessfulCustomerTransaction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Api(tags = "Customer Related Processes")
@RestController
@RequestMapping("v1/agents")
public class CustomersController {
    private final CustomerServiceFactory customerServiceFactory;

    public CustomersController(CustomerServiceFactory customerServiceFactory) {
        this.customerServiceFactory = customerServiceFactory;
    }

    @ApiOperation("Create A Customer")
    @PutMapping("{market}/{agentId}/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> addCustomer(@PathVariable String agentId, @PathVariable Market market,
                                    @RequestBody RawCustomer rawCustomer) {
        return this.customerServiceFactory
                .getServiceForMarket(market)
                .addCustomer(rawCustomer, agentId, market);
    }

    @ApiOperation("Get A Customer By Id and Agent Id")
    @GetMapping("{market}/{agentId}/customers/{customerId}")
    public Mono<Customer> getCustomerByIdAndAgentId(@PathVariable String customerId, @PathVariable Market market,
                                                    @PathVariable String agentId) {
        return this.customerServiceFactory
                .getServiceForMarket(market)
                .getCustomerByIdAndAgentId(customerId, agentId);
    }

    @PatchMapping("{market}/{agentId}/customers/{customerId}")
    public Mono<SuccessfulCustomerTransaction> updateCustomer(@PathVariable String agentId, @PathVariable Market market,
                                                              @PathVariable String customerId, @RequestBody RawCustomer rawCustomer) {
        return this.customerServiceFactory
                .getServiceForMarket(market)
                .updateCustomer(customerId, agentId, rawCustomer, market);
    }

    @GetMapping("{market}/{agentId}/customers/summary")
    public Flux<CustomerSummary> getAllCustomerSummariesWithAgentId(@PathVariable String agentId,
                                                                    @PathVariable Market market,
                                                                    @RequestParam int index,
                                                                    @RequestParam(defaultValue = "20") int size,
                                                                    @RequestParam(defaultValue = "false") boolean deleted) {
        return this.customerServiceFactory
                .getServiceForMarket(market)
                .getAllCustomerSummariesByAgentIdAndPage(agentId, market, index, size, deleted);
    }

    @PostMapping("{market}/{agentId}/customers/{customerId}/restore")
    public Mono<Boolean> restoreCustomerByIdAndAgentId(@PathVariable String agentId, @PathVariable Market market,
                                                       @PathVariable String customerId) {
        return this.customerServiceFactory
                .getServiceForMarket(market)
                .toggleCustomerDeletedStatus(agentId, customerId, market, false);
    }

    @DeleteMapping("{market}/{agentId}/customers/{customerId}")
    public Mono<Boolean> deleteCustomerByIdAndAgentId(@PathVariable String agentId, @PathVariable String customerId,
                                                      @PathVariable Market market) {
        return this.customerServiceFactory
                .getServiceForMarket(market)
                .toggleCustomerDeletedStatus(agentId, customerId, market, true);
    }

    @GetMapping("{market}/{agentId}/customers/customer")
    public Flux<Customer> getCustomersWithIds(@PathVariable String agentId, @PathVariable Market market,
                                              @RequestParam List<String> ids) {
        return this.customerServiceFactory
                .getServiceForMarket(market)
                .getCustomersByIdWithAgentId(agentId, ids);
    }

    @ApiOperation("Get Customer Count By Agent Id")
    @GetMapping("{market}/{agentId}/customer-count")
    public Mono<Integer> getAllCustomerCountByAgentIdAndDeletedStatus(@PathVariable String agentId,
                                                                      @PathVariable Market market,
                                                                      @RequestParam boolean deleted) {
        return this.customerServiceFactory
                .getServiceForMarket(market)
                .getAllCustomerCountByAgentIdAndDeletedStatus(agentId, deleted);
    }

    @GetMapping("{market}/{agentId}/customers/{customerId}/applications")
    public Flux<ApiApplicationMeta> getApplicationsForCustomerWithId(@PathVariable String agentId,
                                                                     @PathVariable String customerId,
                                                                     @PathVariable Market market,
                                                                     @RequestParam int index) {
        return this.customerServiceFactory
                .getServiceForMarket(market)
                .getApplicationsForCustomer(market, customerId, index);
    }

    @GetMapping("{market}/{agentId}/customers/{customerId}/application-count")
    public Mono<Integer> getCustomerApplicationCount(@PathVariable String customerId, @PathVariable Market market) {
        return this.customerServiceFactory
                .getServiceForMarket(market)
                .getCustomerApplicationCount(market, customerId);
    }
}
