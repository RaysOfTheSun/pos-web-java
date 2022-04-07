package com.raysofthesun.poswebjava.apply.applications.services.marketCor;


import com.raysofthesun.poswebjava.apply.applications.factories.ApplicationCreatorServiceFactory;
import com.raysofthesun.poswebjava.apply.applications.repositories.core.ApplicationRepository;
import com.raysofthesun.poswebjava.apply.applications.services.core.ApplicationServiceV2;
import com.raysofthesun.poswebjava.apply.insureds.factories.ApplicationInsuredServiceFactory;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import org.springframework.stereotype.Service;

@Service
public class CorApplicationService extends ApplicationServiceV2 {
    public CorApplicationService(ApplicationRepository applicationRepository,
                                 ApplicationCreatorServiceFactory applicationCreatorServiceFactory,
                                 ApplicationInsuredServiceFactory applicationInsuredServiceFactory) {
        super(applicationRepository, applicationCreatorServiceFactory, applicationInsuredServiceFactory);
    }

    @Override
    public Market getMarket() {
        return Market.COR;
    }
}
