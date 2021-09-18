package com.raysofthesun.poswebjava.propose.services;

import com.raysofthesun.poswebjava.propose.repositories.ProposalRepository;
import org.springframework.stereotype.Service;

@Service
public class ProposeService {
	protected final ProposalRepository repository;

	public ProposeService(ProposalRepository proposalRepository) {
		this.repository = proposalRepository;
	}
}
