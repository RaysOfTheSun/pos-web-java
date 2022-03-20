package com.raysofthesun.poswebjava.customer2.factories;

import com.raysofthesun.poswebjava.core.factories.PosWebServiceFactory;
import com.raysofthesun.poswebjava.customer2.services.core.CustomerCreatorService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerCreatorServiceFactory extends PosWebServiceFactory<CustomerCreatorService> {
    public CustomerCreatorServiceFactory(List<CustomerCreatorService> services) {
        super(services);
    }
}
