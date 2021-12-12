package com.raysofthesun.poswebjava.apply.insured.services;

import com.raysofthesun.poswebjava.apply.insured.models.insured.Insured;
import com.raysofthesun.poswebjava.apply.insured.repositories.InsuredRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Service
public class InsuredService {
	private final InsuredRepository insuredRepository;

	public InsuredService(InsuredRepository insuredRepository) {
		this.insuredRepository = insuredRepository;
	}

	public Flux<Insured> getInsuredsById(Collection<String> insuredIds) {
		return insuredRepository.findAllById(insuredIds);
	}

	public Mono<Insured> saveInsured(Insured insured) {
		return insuredRepository.save(insured);
	}

	public Mono<Boolean> saveAllInsureds(Collection<Insured> insureds) {
		return insuredRepository
				.saveAll(insureds)
				.then(Mono.just(true));

	}
}
