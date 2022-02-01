package com.raysofthesun.poswebjava.agent.customer.services;

import com.raysofthesun.poswebjava.agent.customer.constants.ContactType;
import com.raysofthesun.poswebjava.agent.customer.feign.application.ApplyApplicationsApi;
import com.raysofthesun.poswebjava.agent.customer.feign.application.models.ApiApplicationMeta;
import com.raysofthesun.poswebjava.agent.customer.mappers.CustomerMapper;
import com.raysofthesun.poswebjava.agent.customer.models.CustomerSummary;
import com.raysofthesun.poswebjava.agent.customer.models.RawCustomer;
import com.raysofthesun.poswebjava.agent.customer.models.person.Contact;
import com.raysofthesun.poswebjava.agent.customer.models.person.ContactInfo;
import com.raysofthesun.poswebjava.agent.exception.CustomerNotFoundException;
import com.raysofthesun.poswebjava.agent.customer.models.Customer;
import com.raysofthesun.poswebjava.agent.customer.repositories.CustomerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    protected final CustomerRepository customerRepository;
    private final ApplyApplicationsApi applyApplicationsApi;

    public CustomerService(CustomerRepository customerRepository, ApplyApplicationsApi applyApplicationsApi) {
        this.customerRepository = customerRepository;
        this.applyApplicationsApi = applyApplicationsApi;
    }

    public Mono<String> addCustomerWithAgentId(RawCustomer customer, String agentId) {


        return Mono.fromSupplier(() -> {
                    final Customer baseCustomer = CustomerMapper.MAPPER.mapRawCustomerToCustomer(customer);
                    final int customerAge = getCustomerAge(baseCustomer);

                    final Optional<Contact> primaryEmailAddress = customer.getEmailAddress().length() > 0
                            ? Optional.of(new Contact(customer.getEmailAddress(), "", ContactType.PRIMARY_EMAIL))
                            : Optional.empty();

                    final Optional<Contact> primaryMobileNumber = customer.getEmailAddress().length() > 0
                            ? Optional.of(new Contact(customer.getMobileNumber(), "", ContactType.PRIMARY_MOBILE))
                            : Optional.empty();

                    final ContactInfo customerContactInfo = new ContactInfo();

                    primaryMobileNumber.ifPresent(contact -> customerContactInfo.setMobileNumbers(List.of(contact)));
                    primaryEmailAddress.ifPresent(contact -> customerContactInfo.setEmailAddresses(List.of(contact)));

                    baseCustomer.setAgentId(agentId);
                    baseCustomer.setContactInfo(customerContactInfo);
                    baseCustomer.getPersonalInfo().setAge(customerAge);

                    return baseCustomer;
                })
                .flatMap(customerRepository::save)
                .map(Customer::getId);
    }

    public Mono<Customer> getCustomerByIdAndAgentId(String customerId, String agentId) {
        return customerRepository
                .findByIdAndAgentId(customerId, agentId)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException()));
    }

    public Mono<String> updateCustomer(String customerId, String agentId, Customer customer) {
        return customerRepository
                .existsCustomersByAgentIdAndId(agentId, customerId)
                .flatMap((isExistingCustomer) -> isExistingCustomer ? Mono.just(customer)
                        : Mono.error(new CustomerNotFoundException()))
                .flatMap(updatedCustomer -> {
                    updatedCustomer.setId(customerId);
                    updatedCustomer.setAgentId(agentId);
                    updatedCustomer.getPersonalInfo().setAge(getCustomerAge(customer));
                    return customerRepository.save(updatedCustomer);
                })
                .map(Customer::getId);
    }

    public Flux<Customer> getCustomersByIdWithAgentId(String agentId, Collection<String> customerIds) {
        return customerRepository
                .findByAgentIdAndIdIn(agentId, customerIds)
                .switchIfEmpty(Flux.error(new CustomerNotFoundException()));
    }

    public Flux<Customer> getAllCustomersWithAgentId(String agentId) {
        return customerRepository.findAllByAgentId(agentId);
    }

    public Flux<CustomerSummary> getAllCustomerSummariesByAgentIdAndPage(String agentId, int pageIndex,
                                                                         int pageSize, boolean deleted) {
        return customerRepository.findAllByAgentIdAndDeleted(agentId, deleted, PageRequest.of(pageIndex, pageSize))
                .map(CustomerMapper.MAPPER::mapCustomerToCustomerSummary);
    }

    public Mono<Integer> getAllCustomerCountByAgentIdAndDeletedStatus(String agentId, boolean isDeleted) {
        return customerRepository.countCustomerByAgentIdAndDeleted(agentId, isDeleted);
    }

    public Mono<Boolean> deleteCustomerByIdAndAgentId(String agentId, String customerId) {
        return customerRepository
                .findByIdAndAgentId(customerId, agentId)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException()))
                .map((customer -> {
                    customer.setId(customerId);
                    customer.setDeleted(true);
                    return customer;
                }))
                .flatMap(customerRepository::save)
                .thenReturn(true);

    }

    public Mono<Boolean> restoreCustomerByIdAndAgentId(String agentId, String customerId) {
        return customerRepository
                .findByIdAndAgentId(customerId, agentId)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException()))
                .map(customer -> {
                    customer.setDeleted(false);
                    return customer;
                })
                .flatMap(customerRepository::save)
                .thenReturn(true);
    }

    public Mono<Integer> getCustomerApplicationCount(String customerId) {
        return applyApplicationsApi.getTotalApplicationCountForCustomerById(customerId);
    }

    private int getCustomerAge(Customer customer) {

        if (customer.getPersonalInfo().getDateOfBirth() == null) {
            return -1;
        }

        Instant currDate = Instant.now();
        Instant customerDob = Instant.parse(customer.getPersonalInfo().getDateOfBirth());
        long dobAndCurrDateDiffInDays = customerDob.until(currDate, ChronoUnit.DAYS);

        return (int) (dobAndCurrDateDiffInDays / 365);
    }

    public Flux<ApiApplicationMeta> getApplicationsForCustomer(String customerId, int pageIndex) {
        return applyApplicationsApi.getApplicationsByCustomerId(customerId, pageIndex);
    }
}
