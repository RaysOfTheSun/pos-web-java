package com.raysofthesun.poswebjava.apply.services;

import com.raysofthesun.poswebjava.apply.constants.InsuredRole;
import com.raysofthesun.poswebjava.apply.feign_clients.agent.CustomerApi;
import com.raysofthesun.poswebjava.apply.mappers.CustomerMapper;
import com.raysofthesun.poswebjava.apply.models.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.models.application.Application;
import com.raysofthesun.poswebjava.apply.models.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.models.insured.Insured;
import com.raysofthesun.poswebjava.apply.repositories.ApplicationRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

	protected final CustomerApi customerApi;
	protected final RabbitTemplate rabbitTemplate;
	protected final InsuredService insuredService;
	protected final ApplicationRepository applicationRepository;

	public ApplicationService(ApplicationRepository applicationRepository, CustomerApi customerApi,
	                          RabbitTemplate rabbitTemplate, InsuredService insuredService) {
		this.customerApi = customerApi;
		this.rabbitTemplate = rabbitTemplate;
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

	public Flux<Application> getAllApplicationsWithCustomerId(String customerId) {
		return applicationRepository
				.findAllByCustomerId(customerId)
				.flatMap(this::createApplicationFromMeta);
	}

	public Flux<ApplicationMeta> getApplicationMetasWithCustomerId(String customerId) {
		return applicationRepository.findAllByCustomerId(customerId);
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

					return Application
							.create(meta)
							.withOwner(idToInsuredMap.getOrDefault(meta.getOwnerId(), null))
							.withInsured(idToInsuredMap.getOrDefault(meta.getInsuredId(), null))
							.withDependents(dependents)
							.withPaymentInfo(meta.getPaymentInfo())
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
				.map(CustomerMapper.MAPPER::mapCustomerToInsured)
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
