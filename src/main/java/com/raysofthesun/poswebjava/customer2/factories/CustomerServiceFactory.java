package com.raysofthesun.poswebjava.customer2.factories;

import com.raysofthesun.poswebjava.core.factories.PosWebServiceFactory;
import com.raysofthesun.poswebjava.customer2.services.core.CustomerService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerServiceFactory extends PosWebServiceFactory<CustomerService> {
    public CustomerServiceFactory(List<CustomerService> services) {
        super(services);
    }
}
