package com.raysofthesun.poswebjava.apply.applications.services.core;

import com.raysofthesun.poswebjava.apply.insureds.factories.ApplicationInsuredServiceFactory;
import com.raysofthesun.poswebjava.apply.insureds.models.core.insured.Insured;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.Application;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.core.enums.Market;
import com.raysofthesun.poswebjava.core.services.PosWebService;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ApplicationCreatorService implements PosWebService {
    protected final ApplicationInsuredServiceFactory insuredServiceFactory;

    public ApplicationCreatorService(ApplicationInsuredServiceFactory insuredServiceFactory) {
        this.insuredServiceFactory = insuredServiceFactory;
    }

    public Mono<Application> createApplicationFromMeta(ApplicationMeta meta, Market market) {
        return this.insuredServiceFactory
                .getServiceForMarket(market)
                .getInsuredsWithApplicationMeta(meta)
                .collectMap(Insured::getId)
                .map(idToInsuredMap -> {
                    List<Insured> dependents = idToInsuredMap
                            .values()
                            .stream()
                            .filter(insured -> meta.getDependentIds().contains(insured.getId()))
                            .collect(Collectors.toList());

                    Application.Builder rawApplication = Application
                            .create(meta)
                            .withDependents(dependents)
                            .withPaymentInfo(meta.getPaymentInfo())
                            .withOwner(idToInsuredMap.getOrDefault(meta.getOwnerId(), null));

                    if (meta.getInsuredId().equals(meta.getOwnerId())) {
                        return rawApplication.build();
                    }

                    return rawApplication
                            .withInsured(idToInsuredMap.getOrDefault(meta.getInsuredId(), null))
                            .build();
                });
    }

    public Mono<Application> createApplicationFromRequest(ApplicationCreationRequest request, String agentId, Market market) {
        return this.insuredServiceFactory
                .getServiceForMarket(Market.COR)
                .getCustomersAsInsuredsFromRequest(request, agentId, market)
                .collectMap(Insured::getId)
                .map((insuredIdMap) -> {
                    boolean isPolicyOwnerInsured = request
                            .getPolicyOwnerId()
                            .equals(request.getPrimaryInsuredId());

                    List<Insured> dependents = insuredIdMap
                            .values()
                            .stream()
                            .filter((insured -> request.getDependentIds().contains(insured.getCustomerId())))
                            .collect(Collectors.toList());

                    Application.Builder applicationBuilder = Application
                            .create(request)
                            .withDependents(dependents)
                            .withTotalPremium(request.getTotalPremium());

                    if (isPolicyOwnerInsured) {
                        return applicationBuilder
                                .withOwner(insuredIdMap.getOrDefault(request.getPolicyOwnerId(), null))
                                .build();
                    }

                    return applicationBuilder
                            .withOwner(insuredIdMap.getOrDefault(request.getPolicyOwnerId(), null))
                            .withInsured(insuredIdMap.getOrDefault(request.getPrimaryInsuredId(), null))
                            .build();
                });
    }

}
