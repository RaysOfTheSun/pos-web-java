package com.raysofthesun.poswebjava.customer.services.core;

import com.raysofthesun.poswebjava.customer.exceptions.CustomerNotFoundException;
import com.raysofthesun.poswebjava.core.enums.Market;
import com.raysofthesun.poswebjava.core.services.PosWebService;
import com.raysofthesun.poswebjava.customer.feign.application.ApplyApplicationsApi;
import com.raysofthesun.poswebjava.customer.feign.application.models.ApiApplicationMeta;
import com.raysofthesun.poswebjava.customer.models.Customer;
import com.raysofthesun.poswebjava.customer.models.CustomerSummary;
import com.raysofthesun.poswebjava.customer.models.RawCustomer;
import com.raysofthesun.poswebjava.customer.repositories.CustomerRepository;
import com.raysofthesun.poswebjava.customer.factories.CustomerCreatorServiceFactory;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public abstract class CustomerService implements PosWebService {
    protected final CustomerRepository customerRepository;
    protected final ApplyApplicationsApi applyApplicationsApi;
    protected final CustomerCreatorServiceFactory creatorServiceFactory;

    public CustomerService(ApplyApplicationsApi applyApplicationsApi,
                           CustomerRepository customerRepository, CustomerCreatorServiceFactory creatorServiceFactory) {
        this.customerRepository = customerRepository;
        this.applyApplicationsApi = applyApplicationsApi;
        this.creatorServiceFactory = creatorServiceFactory;
    }

    public abstract Market getMarket();

    public Mono<String> addCustomer(RawCustomer rawCustomer, String agentId, Market market) {
        return Mono.fromSupplier(() -> this.creatorServiceFactory
                        .getServiceForMarket(market).createFromRawCustomer(rawCustomer, agentId))
                .flatMap(this.customerRepository::save)
                .map(Customer::getId);
    }

    public Mono<String> updateCustomer(String customerId, String agentId, RawCustomer rawCustomer, Market market) {
        return this
                .validateCustomerExistence(customerId, agentId)
                .map((e) -> this.creatorServiceFactory.getServiceForMarket(market)
                        .createFromRawCustomer(rawCustomer, agentId, customerId))
                .flatMap(this.customerRepository::save)
                .map(Customer::getId);
    }

    public Mono<Boolean> toggleCustomerDeletedStatus(String agentId, String customerId, Market market, boolean isDeleted) {
        return this.customerRepository.findByIdAndAgentId(agentId, customerId)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException()))
                .map(customer -> this.creatorServiceFactory
                        .getServiceForMarket(market)
                        .createWithDeletedStatus(customer, customerId, isDeleted))
                .flatMap(this.customerRepository::save)
                .thenReturn(true);
    }

    public Mono<Customer> getCustomerByIdAndAgentId(String customerId, String agentId) {
        return customerRepository
                .findByIdAndAgentId(customerId, agentId)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException()));
    }

    public Flux<CustomerSummary> getAllCustomerSummariesByAgentIdAndPage(String agentId, Market market, int pageIndex,
                                                                         int pageSize, boolean deleted) {
        return this.customerRepository
                .findAllByAgentIdAndDeleted(agentId, deleted, PageRequest.of(pageIndex, pageSize))
                .map(this.creatorServiceFactory.getServiceForMarket(market)::createSummaryFromCustomer);
    }

    public Mono<Integer> getAllCustomerCountByAgentIdAndDeletedStatus(String agentId, boolean isDeleted) {
        return customerRepository.countCustomerByAgentIdAndDeleted(agentId, isDeleted);
    }

    protected Mono<Boolean> validateCustomerExistence(String customerId, String agentId) {
        return this.customerRepository
                .existsCustomersByAgentIdAndId(agentId, customerId)
                .flatMap(doesCustomerExist -> doesCustomerExist
                        ? Mono.just(true)
                        : Mono.error(new CustomerNotFoundException())
                );
    }

    public Flux<ApiApplicationMeta> getApplicationsForCustomer(String customerId, int pageIndex) {
        return this.applyApplicationsApi.getApplicationsByCustomerId(customerId, pageIndex);
    }

    public Mono<Integer> getCustomerApplicationCount(String customerId) {
        return this.applyApplicationsApi.getTotalApplicationCountForCustomerById(customerId);
    }

    public Flux<Customer> getCustomersByIdWithAgentId(String agentId, Collection<String> customerIds) {
        return customerRepository
                .findByAgentIdAndIdIn(agentId, customerIds)
                .switchIfEmpty(Flux.error(new CustomerNotFoundException()));
    }
}
