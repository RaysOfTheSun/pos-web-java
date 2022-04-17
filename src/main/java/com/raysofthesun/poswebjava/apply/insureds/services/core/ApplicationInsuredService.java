package com.raysofthesun.poswebjava.apply.insureds.services.core;

import com.raysofthesun.poswebjava.apply.feign.CustomerApi;
import com.raysofthesun.poswebjava.apply.insureds.enums.InsuredRole;
import com.raysofthesun.poswebjava.apply.insureds.mappers.ApplicationInsuredMapper;
import com.raysofthesun.poswebjava.apply.insureds.models.core.insured.Insured;
import com.raysofthesun.poswebjava.apply.insureds.repositories.InsuredRepository;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.core.common.services.PosWebService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Optional;

public abstract class ApplicationInsuredService implements PosWebService {
    protected CustomerApi customerApi;
    protected InsuredRepository insuredRepository;

    public abstract Market getMarket();

    public ApplicationInsuredService(CustomerApi customerApi, InsuredRepository insuredRepository) {
        this.customerApi = customerApi;
        this.insuredRepository = insuredRepository;
    }

    public Mono<Insured> getInsuredById(String insuredId) {
        return insuredRepository.findById(insuredId);
    }

    public Mono<Insured> saveInsured(Insured insured) {
        return insuredRepository.save(insured);
    }

    public Mono<Boolean> saveAllInsureds(Collection<Insured> insureds) {
        return insuredRepository
                .saveAll(insureds)
                .then(Mono.just(true));
    }

    public Flux<Insured> getInsuredsWithApplicationMeta(ApplicationMeta applicationMeta) {
        return Flux
                .fromIterable(applicationMeta.getDependentIds())
                .mergeWith(Flux.just(applicationMeta.getOwnerId(), applicationMeta.getInsuredId()))
                .filterWhen((insuredId) -> Mono.just(Optional.ofNullable(insuredId).isPresent()))
                .collectList()
                .flatMapMany((insuredIds) -> this.insuredRepository.findAllById(insuredIds));
    }

    public Flux<Insured> getCustomersAsInsuredsFromRequest(ApplicationCreationRequest request, String customerAgentId,
                                                           Market market) {
        return Flux
                .fromIterable(request.getDependentIds())
                .mergeWith(Flux.just(request.getPolicyOwnerId(), request.getPrimaryInsuredId()))
                .collectList()
                .flatMapMany(customerIds -> this.customerApi
                        .getCustomersByIdAndAgentId(customerAgentId, customerIds, market))
                .map(apiCustomer -> {
                    Insured customerAsInsured = ApplicationInsuredMapper.MAPPER.mapApiCustomerToInsured(apiCustomer);
                    InsuredRole customerAsInsuredRole = this.getInsuredRoleByRequest(request, apiCustomer.getId());

                    customerAsInsured.setRole(customerAsInsuredRole);

                    return customerAsInsured;
                }).flatMap(insuredRepository::save);
    }

    protected InsuredRole getInsuredRoleByRequest(ApplicationCreationRequest request, String customerId) {
        boolean isPolicyOwner = request.getPolicyOwnerId().equals(customerId);
        boolean isInsuredDependent = request.getDependentIds().contains(customerId);
        boolean isPrimaryInsuredOwner = request.getPrimaryInsuredId().equals(request.getPolicyOwnerId());

        if (isInsuredDependent) {
            return InsuredRole.OI;
        }

        if (isPolicyOwner && isPrimaryInsuredOwner) {
            return InsuredRole.IO;
        }

        return isPolicyOwner ? InsuredRole.PO : InsuredRole.PI;
    }
}
