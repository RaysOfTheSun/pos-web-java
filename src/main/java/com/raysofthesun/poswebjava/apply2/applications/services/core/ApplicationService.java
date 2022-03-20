package com.raysofthesun.poswebjava.apply2.applications.services.core;

import com.raysofthesun.poswebjava.apply2.insureds.enums.InsuredRole;
import com.raysofthesun.poswebjava.apply2.feign.CustomerApi;
import com.raysofthesun.poswebjava.apply.insured.services.InsuredService;
import com.raysofthesun.poswebjava.apply2.insureds.mappers.ApplicationInsuredMapper;
import com.raysofthesun.poswebjava.apply2.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply2.applications.repositories.core.ApplicationRepository;
import com.raysofthesun.poswebjava.apply2.applications.models.core.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply2.applications.models.core.application.Application;
import com.raysofthesun.poswebjava.apply2.insureds.models.core.insured.Insured;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ApplicationService {

    protected final CustomerApi customerApi;
    protected final InsuredService insuredService;
    protected final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository, CustomerApi customerApi,
                              InsuredService insuredService) {
        this.customerApi = customerApi;
        this.insuredService = insuredService;
        this.applicationRepository = applicationRepository;
    }

    public Mono<Application> createApplicationWithRequestAndAgentId(
            ApplicationCreationRequest request, String agentId) {
        return createApplicationWithRequest(request, agentId)
                .flatMap(this::createApplicationMetaAndSave);
    }

    public Mono<Application> getApplicationWithId(String applicationId) {
        return applicationRepository
                .findById(applicationId)
                .flatMap(this::createApplicationFromMeta);
    }

    public Mono<Boolean> updateApplication(String applicationId, Application updatedApplication) {
        return applicationRepository.findById(applicationId)
                .switchIfEmpty(Mono.error(new RuntimeException()))
                .flatMap(applicationMeta -> {
                    applicationMeta.setLastModifiedDate(Instant.now().toString());
                    applicationMeta.setPaymentInfo(updatedApplication.getPaymentInfo());
                    return applicationRepository.save(applicationMeta);
                }).flatMap(app -> updateInsuredsInApplication(updatedApplication));


    }

    private Mono<Boolean> updateInsuredsInApplication(Application application) {
        return Flux.
                fromIterable(application.getDependents())
                .mergeWith(Flux.just(application.getOwner(), application.getInsured()))
                .filterWhen((insured -> Mono.just(Optional.ofNullable(insured).isPresent())))
                .collectList()
                .flatMap(insuredService::saveAllInsureds);

    }

    public Mono<Integer> getTotalApplicationCountForCustomerById(String customerId) {
        return applicationRepository.countApplicationMetasByCustomerId(customerId);
    }

//    public Flux<Application> getAllApplicationsWithCustomerId(String customerId) {
//        return applicationRepository
//                .findAllByCustomerId(customerId)
//                .flatMap(this::createApplicationFromMeta);
//    }

    public Flux<ApplicationMeta> getApplicationMetasWithCustomerId(String customerId, int pageIndex, int pageSize) {
        return applicationRepository.findAllByCustomerId(customerId, PageRequest.of(pageIndex, pageSize));
    }

    protected Mono<Application> createApplicationFromMeta(ApplicationMeta meta) {
        return this
                .getAllInsuredsInApplication(meta)
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

    protected Flux<Insured> getAllInsuredsInApplication(ApplicationMeta meta) {
        return Flux
                .fromIterable(meta.getDependentIds())
                .mergeWith(Flux.just(meta.getOwnerId(), meta.getInsuredId()))
                .filterWhen((id) -> Mono.just(id != null))
                .collectList()
                .flatMapMany(this.insuredService::getInsuredsById);
    }

    protected Mono<Application> createApplicationMetaAndSave(Application application) {
        return Mono
                .just(ApplicationMeta.create(application).build())
                .flatMap(applicationRepository::save)
                .map((applicationMeta) -> application);
    }

    protected Mono<Application> createApplicationWithRequest(ApplicationCreationRequest request, String agentId) {
        return getInsuredsForApplicationByRequest(request, agentId)
                .collectMap((Insured::getId))
                .map((insuredIdMap -> {

                    final boolean isPolicyOwnerInsured = request
                            .getPolicyOwnerId()
                            .equals(request.getPrimaryInsuredId());

                    final List<Insured> dependents = insuredIdMap
                            .values()
                            .stream()
                            .filter((insured -> request.getDependentIds().contains(insured.getCustomerId())))
                            .collect(Collectors.toList());

                    Application.Builder builder = Application
                            .create(request)
                            .withDependents(dependents)
                            .withTotalPremium(request.getTotalPremium());

                    if (isPolicyOwnerInsured) {
                        return builder
                                .withOwner(insuredIdMap.getOrDefault(request.getPolicyOwnerId(), null))
                                .build();
                    }

                    return builder
                            .withOwner(insuredIdMap.getOrDefault(request.getPolicyOwnerId(), null))
                            .withInsured(insuredIdMap.getOrDefault(request.getPrimaryInsuredId(), null))
                            .build();
                }));
    }

    protected Flux<Insured> getInsuredsForApplicationByRequest(ApplicationCreationRequest request, String agentId) {
        return getCustomerIdsFromRequest(request)
                .collectList()
                .flatMap((customerIds) -> customerApi.getCustomersByIdAndAgentId(agentId, customerIds).collectList())
                .flatMapMany(Flux::fromIterable)
                .map(ApplicationInsuredMapper.MAPPER::mapApiCustomerToInsured)
                .map((insured -> assignRoleToInsuredByCreationRequest(request, insured)))
                .flatMap(insuredService::saveInsured);
    }

    protected Flux<String> getCustomerIdsFromRequest(ApplicationCreationRequest request) {
        return Flux
                .fromIterable(request.getDependentIds())
                .mergeWith(Flux.just(request.getPolicyOwnerId(), request.getPrimaryInsuredId()))
                .filterWhen((customerId) -> Mono.just(!customerId.isEmpty()));
    }

    protected Insured assignRoleToInsuredByCreationRequest(ApplicationCreationRequest request, Insured insured) {
        final InsuredRole role = getInsuredRoleByCreationRequest(request, insured);
        insured.setRole(role);

        return insured;
    }

    protected InsuredRole getInsuredRoleByCreationRequest(ApplicationCreationRequest request, Insured insured) {
        final boolean isPrimaryInsuredOwner = request.getPrimaryInsuredId().equals(request.getPolicyOwnerId());
        final boolean isInsuredDependent = request.getDependentIds().contains(insured.getCustomerId());

        if (isPrimaryInsuredOwner && insured.getCustomerId().equals(request.getPolicyOwnerId())) {
            return InsuredRole.IO;
        }

        if (isInsuredDependent) {
            return InsuredRole.OI;
        }

        return request.getPolicyOwnerId().equals(insured.getCustomerId())
                ? InsuredRole.PO
                : InsuredRole.PI;
    }
}
