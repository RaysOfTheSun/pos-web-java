package com.raysofthesun.poswebjava.customer.services.core;

import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.core.common.services.PosWebService;
import com.raysofthesun.poswebjava.core.common.utilities.DateTime;
import com.raysofthesun.poswebjava.customer.mappers.CustomerMapper;
import com.raysofthesun.poswebjava.customer.models.Customer;
import com.raysofthesun.poswebjava.customer.models.CustomerSummary;
import com.raysofthesun.poswebjava.customer.models.RawCustomer;

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

