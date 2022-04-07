package com.raysofthesun.poswebjava.apply.insureds.services.marketCor;

import com.raysofthesun.poswebjava.apply.insureds.repositories.InsuredRepository;
import com.raysofthesun.poswebjava.apply.insureds.services.core.ApplicationInsuredService;
import com.raysofthesun.poswebjava.apply.feign.CustomerApi;
import com.raysofthesun.poswebjava.core.common.enums.Market;
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
