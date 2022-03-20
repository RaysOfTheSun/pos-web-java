package com.raysofthesun.poswebjava.apply2.applications.services.marketCor;


import com.raysofthesun.poswebjava.apply2.applications.factories.ApplicationCreatorServiceFactory;
import com.raysofthesun.poswebjava.apply2.applications.repositories.core.ApplicationRepository;
import com.raysofthesun.poswebjava.apply2.applications.services.core.ApplicationServiceV2;
import com.raysofthesun.poswebjava.core.enums.Market;
import org.springframework.stereotype.Service;

@Service
public class CorApplicationService extends ApplicationServiceV2 {
    public CorApplicationService(ApplicationRepository applicationRepository,
                                 ApplicationCreatorServiceFactory applicationCreatorServiceFactory) {
        super(applicationRepository, applicationCreatorServiceFactory);
    }

    @Override
    public Market getMarket() {
        return Market.COR;
    }
}
