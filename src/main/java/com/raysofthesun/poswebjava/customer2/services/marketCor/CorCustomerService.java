package com.raysofthesun.poswebjava.customer2.services.marketCor;

import com.raysofthesun.poswebjava.core.enums.Market;
import com.raysofthesun.poswebjava.customer2.feign.application.ApplyApplicationsApi;
import com.raysofthesun.poswebjava.customer2.repositories.CustomerRepository;
import com.raysofthesun.poswebjava.customer2.factories.CustomerCreatorServiceFactory;
import com.raysofthesun.poswebjava.customer2.services.core.CustomerService;
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
