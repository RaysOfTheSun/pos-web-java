package com.raysofthesun.poswebjava.apply.applications.services.core;

import com.raysofthesun.poswebjava.apply.applications.factories.ApplicationCreatorServiceFactory;
import com.raysofthesun.poswebjava.apply.applications.factories.ApplicationDocumentServiceFactory;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.Application;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.applications.repositories.core.ApplicationRepository;
import com.raysofthesun.poswebjava.apply.insureds.factories.ApplicationInsuredServiceFactory;
import com.raysofthesun.poswebjava.apply.insureds.models.core.insured.Insured;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.core.common.services.PosWebService;
import com.raysofthesun.poswebjava.core.configuration.models.PosDocumentRequirement;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class ApplicationServiceV2 implements PosWebService {
    protected final ApplicationRepository applicationRepository;
    protected final ApplicationCreatorServiceFactory applicationCreatorServiceFactory;
    protected final ApplicationInsuredServiceFactory applicationInsuredServiceFactory;
    protected final ApplicationDocumentServiceFactory applicationDocumentServiceFactory;

    public ApplicationServiceV2(ApplicationRepository applicationRepository,
                                ApplicationCreatorServiceFactory applicationCreatorServiceFactory,
                                ApplicationInsuredServiceFactory applicationInsuredServiceFactory,
                                ApplicationDocumentServiceFactory applicationDocumentServiceFactory) {
        this.applicationRepository = applicationRepository;
        this.applicationCreatorServiceFactory = applicationCreatorServiceFactory;
        this.applicationInsuredServiceFactory = applicationInsuredServiceFactory;
        this.applicationDocumentServiceFactory = applicationDocumentServiceFactory;
    }

    public Mono<Application> getApplicationById(String applicationId, Market market) {
        return this
                .applicationRepository
                .findById(applicationId)
                .flatMap(applicationMeta -> this.applicationCreatorServiceFactory
                        .getServiceForMarket(market).createApplicationFromMeta(applicationMeta, market));
    }

    public Flux<ApplicationMeta> getApplicationMetasByCustomerId(String customerId, int pageIndex, int pageSize) {
        return this.applicationRepository.findAllByCustomerId(customerId, PageRequest.of(pageIndex, pageSize));
    }

    public Mono<Integer> getTotalApplicationCountForCustomerById(String customerId) {
        return this.applicationRepository.countApplicationMetasByCustomerId(customerId);
    }

    public Mono<Application> createApplicationFromRequestAndAgentId(ApplicationCreationRequest request,
                                                                    String agentId,
                                                                    Market market) {
        return this
                .applicationCreatorServiceFactory
                .getServiceForMarket(Market.COR)
                .createApplicationFromRequest(request, agentId, market)
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

    public Mono<Map<String, List<PosDocumentRequirement>>> getDocumentRequirementsForInsuredById(String applicationId,
                                                                                                 Market market) {
        return this.applicationRepository.findById(applicationId)
                .flatMap(applicationMeta -> this.applicationInsuredServiceFactory
                        .getServiceForMarket(market)
                        .getInsuredsWithApplicationMeta(applicationMeta)
                        .collectList()
                        .map(insureds -> this.applicationDocumentServiceFactory
                                .getServiceForMarket(market)
                                .getRequiredDocsForApplication(applicationMeta, insureds)));
    }
}
