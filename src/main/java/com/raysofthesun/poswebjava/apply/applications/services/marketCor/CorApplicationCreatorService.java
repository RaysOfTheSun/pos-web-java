package com.raysofthesun.poswebjava.apply.applications.services.marketCor;

import com.raysofthesun.poswebjava.apply.applications.services.core.ApplicationCreatorService;
import com.raysofthesun.poswebjava.apply.insureds.factories.ApplicationInsuredServiceFactory;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import org.springframework.stereotype.Service;

@Service
public class CorApplicationCreatorService extends ApplicationCreatorService {
    public CorApplicationCreatorService(ApplicationInsuredServiceFactory insuredServiceFactory) {
        super(insuredServiceFactory);
    }

    @Override
    public Market getMarket() {
        return Market.COR;
    }
}
