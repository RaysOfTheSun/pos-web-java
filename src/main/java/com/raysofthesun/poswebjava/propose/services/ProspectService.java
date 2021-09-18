package com.raysofthesun.poswebjava.propose.services;

import com.raysofthesun.poswebjava.propose.models.prospect.Prospect;
import com.raysofthesun.poswebjava.propose.repositories.ProspectRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProspectService {
	ProspectRepository repository;

	public ProspectService(ProspectRepository prospectRepository) {
		this.repository = prospectRepository;
	}

	public Mono<Prospect> getProspectById(String prospectId) {
		return repository
				.findById(prospectId)
				.switchIfEmpty(Mono.error(new RuntimeException("NOT FOUND")));
	}

	public Mono<String> addProspect(Prospect prospect) {
		return repository
				.save(prospect)
				.map(Prospect::getId);
	}

	public Mono<String> deleteProspectById(String prospectId) {
		return repository.existsById(prospectId)
				.flatMap((isExistingProspect) -> isExistingProspect ? Mono.just(prospectId) : Mono.empty())
				.switchIfEmpty(Mono.error(new RuntimeException("NOT FOUND")))
				.flatMap((idOfProspectToDelete) -> repository.deleteById(idOfProspectToDelete))
				.thenReturn(prospectId);
	}
}
