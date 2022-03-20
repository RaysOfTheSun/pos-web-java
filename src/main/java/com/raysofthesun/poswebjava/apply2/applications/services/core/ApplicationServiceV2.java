package com.raysofthesun.poswebjava.apply2.applications.services.core;

import com.raysofthesun.poswebjava.apply2.applications.factories.ApplicationCreatorServiceFactory;
import com.raysofthesun.poswebjava.apply2.applications.models.core.application.Application;
import com.raysofthesun.poswebjava.apply2.applications.models.core.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply2.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply2.applications.repositories.core.ApplicationRepository;
import com.raysofthesun.poswebjava.core.enums.Market;
import com.raysofthesun.poswebjava.core.services.PosWebService;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class ApplicationServiceV2 implements PosWebService {
    protected final ApplicationRepository applicationRepository;
    protected final ApplicationCreatorServiceFactory applicationCreatorServiceFactory;

    public ApplicationServiceV2(ApplicationRepository applicationRepository,
                                ApplicationCreatorServiceFactory applicationCreatorServiceFactory) {
        this.applicationRepository = applicationRepository;
        this.applicationCreatorServiceFactory = applicationCreatorServiceFactory;
    }

    public abstract Market getMarket();

    public Mono<Application> getApplicationById(String applicationId) {
        return this
                .applicationRepository
                .findById(applicationId)
                .flatMap(this.applicationCreatorServiceFactory
                        .getServiceForMarket(Market.COR)::createApplicationFromMeta);
    }

    public Flux<ApplicationMeta> getApplicationMetasByCustomerId(String customerId, int pageIndex, int pageSize) {
        return this.applicationRepository.findAllByCustomerId(customerId, PageRequest.of(pageIndex, pageSize));
    }

    public Mono<Integer> getTotalApplicationCountForCustomerById(String customerId) {
        return this.applicationRepository.countApplicationMetasByCustomerId(customerId);
    }

    public Mono<Application> createApplicationFromRequestAndAgentId(ApplicationCreationRequest request,
                                                                    String agentId) {
        return this
                .applicationCreatorServiceFactory
                .getServiceForMarket(Market.COR)
                .createApplicationFromRequest(request, agentId)
                .flatMap((application -> {
                    ApplicationMeta applicationMeta = ApplicationMeta
                            .create(application)
                            .build();

                    return this
                            .applicationRepository
                            .save(applicationMeta)
                            .map(applicationId -> application);
                }));

    }
}
