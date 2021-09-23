package com.raysofthesun.poswebjava.apply.services;

import com.raysofthesun.poswebjava.apply.models.application.Application;
import com.raysofthesun.poswebjava.apply.models.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.repositories.ApplicationRepository;
import com.raysofthesun.poswebjava.propose.models.proposal.Proposal;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ApplicationService {
	protected ApplicationRepository applicationRepository;

	public ApplicationService(ApplicationRepository applicationRepository) {
		this.applicationRepository = applicationRepository;
	}

//	public Mono<Application> createApplicationFromProposal(Proposal proposal) {
//		return Mono.just();
//	}

	public Mono<ApplicationMeta> createApplicationMetaFromProposal(Proposal proposal) {
		return Mono.just(ApplicationMeta.create(proposal).build());
	}
}
