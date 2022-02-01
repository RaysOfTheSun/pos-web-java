package com.raysofthesun.poswebjava.agent.customer.controllers;

import com.raysofthesun.poswebjava.agent.customer.feign.application.models.ApiApplicationMeta;
import com.raysofthesun.poswebjava.agent.customer.models.Customer;
import com.raysofthesun.poswebjava.agent.customer.models.CustomerSummary;
import com.raysofthesun.poswebjava.agent.customer.models.RawCustomer;
import com.raysofthesun.poswebjava.agent.customer.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("v1/agents")
public class CustomerController {

    final protected CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PutMapping("/{agentId}/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> addCustomer(@PathVariable String agentId, @RequestBody RawCustomer rawCustomer) {
        return customerService.addCustomerWithAgentId(rawCustomer, agentId);
    }

    @GetMapping("/{agentId}/customers/{customerId}")
    public Mono<Customer> getCustomerByIdAndAgentId(@PathVariable String customerId, @PathVariable String agentId) {
        return customerService.getCustomerByIdAndAgentId(customerId, agentId);
    }

    @PatchMapping("/{agentId}/customers/{customerId}")
    public Mono<String> updateCustomer(@PathVariable String agentId, @PathVariable String customerId,
                                       @RequestBody Customer customer) {
        return customerService.updateCustomer(customerId, agentId, customer);
    }

    @GetMapping("/{agentId}/customers/summary")
    public Flux<CustomerSummary> getAllCustomerSummariesWithAgentId(@PathVariable String agentId,
                                                                    @RequestParam int index,
                                                                    @RequestParam(defaultValue = "20") int size,
                                                                    @RequestParam(defaultValue = "false") boolean deleted) {
        return customerService.getAllCustomerSummariesByAgentIdAndPage(agentId, index, size, deleted);
    }

    @PostMapping("/{agentId}/customers/{customerId}/restore")
    public Mono<Boolean> restoreCustomerByIdAndAgentId(@PathVariable String agentId, @PathVariable String customerId) {
        return customerService.restoreCustomerByIdAndAgentId(agentId, customerId);
    }

    @DeleteMapping("/{agentId}/customers/{customerId}")
    public Mono<Boolean> deleteCustomerByIdAndAgentId(@PathVariable String agentId, @PathVariable String customerId) {
        return customerService.deleteCustomerByIdAndAgentId(agentId, customerId);
    }

    @GetMapping("/{agentId}/customers/customer")
    public Flux<Customer> getCustomersWithIds(@PathVariable String agentId, @RequestParam List<String> ids) {
        return customerService.getCustomersByIdWithAgentId(agentId, ids);
    }

    @GetMapping("/{agentId}/customer-count")
    public Mono<Integer> getAllCustomerCountByAgentIdAndDeletedStatus(@PathVariable String agentId,
                                                                      @RequestParam boolean deleted) {
        return customerService.getAllCustomerCountByAgentIdAndDeletedStatus(agentId, deleted);
    }

    @GetMapping("/{agentId}/customers/{customerId}/applications")
    public Flux<ApiApplicationMeta> getApplicationsForCustomerWithId(@PathVariable String agentId,
                                                                     @PathVariable String customerId,
                                                                     @RequestParam int index
    ) {
        return customerService.getApplicationsForCustomer(customerId, index);
    }

    @GetMapping("/{agentId}/customers/{customerId}/application-count")
    public Mono<Integer> getCustomerApplicationCount(@PathVariable String customerId) {
        return customerService.getCustomerApplicationCount(customerId);
    }
}
