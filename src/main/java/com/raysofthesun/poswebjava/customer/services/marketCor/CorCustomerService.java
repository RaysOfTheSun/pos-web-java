package com.raysofthesun.poswebjava.customer.services.marketCor;

import com.raysofthesun.poswebjava.core.enums.Market;
import com.raysofthesun.poswebjava.customer.feign.application.ApplyApplicationsApi;
import com.raysofthesun.poswebjava.customer.repositories.CustomerRepository;
import com.raysofthesun.poswebjava.customer.factories.CustomerCreatorServiceFactory;
import com.raysofthesun.poswebjava.customer.services.core.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CorCustomerService extends CustomerService {
    public CorCustomerService(ApplyApplicationsApi applyApplicationsApi, CustomerRepository customerRepository,
                              CustomerCreatorServiceFactory creatorServiceFactory) {
        super(applyApplicationsApi, customerRepository, creatorServiceFactory);
    }

    @Override
    public Market getMarket() {
        return Market.COR;
    }
}
