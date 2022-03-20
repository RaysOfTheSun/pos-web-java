package com.raysofthesun.poswebjava.apply2.insureds.services.marketCor;

import com.raysofthesun.poswebjava.apply2.insureds.repositories.InsuredRepository;
import com.raysofthesun.poswebjava.apply2.insureds.services.core.ApplicationInsuredService;
import com.raysofthesun.poswebjava.apply2.feign.CustomerApi;
import com.raysofthesun.poswebjava.core.enums.Market;
import org.springframework.stereotype.Service;

@Service
public class CorApplicationInsuredService extends ApplicationInsuredService {

    @Override
    public Market getMarket() {
        return Market.COR;
    }

    public CorApplicationInsuredService(CustomerApi customerApi, InsuredRepository insuredRepository) {
        super(customerApi, insuredRepository);
    }
}
