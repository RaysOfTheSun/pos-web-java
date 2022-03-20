package com.raysofthesun.poswebjava.customer2.services.core;

import com.raysofthesun.poswebjava.core.enums.Market;
import com.raysofthesun.poswebjava.core.services.PosWebService;
import com.raysofthesun.poswebjava.core.utilities.DateTime;
import com.raysofthesun.poswebjava.customer2.mappers.CustomerMapper;
import com.raysofthesun.poswebjava.customer2.models.Customer;
import com.raysofthesun.poswebjava.customer2.models.CustomerSummary;
import com.raysofthesun.poswebjava.customer2.models.RawCustomer;

public abstract class CustomerCreatorService implements PosWebService {
    public abstract Market getMarket();

    public Customer createWithDeletedStatus(Customer customer, String customerId, boolean deletedStatus) {
        customer.setId(customerId);
        customer.setDeleted(deletedStatus);

        return customer;
    }

    public Customer createFromRawCustomer(RawCustomer rawCustomer, String agentId) {
        Customer customer = this.createFromRawCustomer(rawCustomer);
        customer.setAgentId(agentId);

        return customer;
    }

    public Customer createFromRawCustomer(RawCustomer rawCustomer, String customerId, String agentId) {
        Customer customer = this.createFromRawCustomer(rawCustomer);
        customer.setId(customerId);
        customer.setAgentId(agentId);

        return customer;
    }

    public Customer createFromRawCustomer(RawCustomer rawCustomer) {
        Customer rawCustomerAsCustomer = CustomerMapper.MAPPER.mapRawCustomerToCustomer(rawCustomer);
        String customerDob = rawCustomerAsCustomer.getPersonalInfo().getDateOfBirth();
        int updatedCustomerAge = new DateTime().fromToday(customerDob);

        rawCustomerAsCustomer.getPersonalInfo().setAge(updatedCustomerAge);

        return rawCustomerAsCustomer;
    }

    public CustomerSummary createSummaryFromCustomer(Customer customer) {
        return CustomerMapper.MAPPER.mapCustomerToCustomerSummary(customer);
    }
}

