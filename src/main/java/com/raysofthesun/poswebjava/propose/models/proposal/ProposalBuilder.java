package com.raysofthesun.poswebjava.propose.models.proposal;

import java.util.Arrays;

public class ProposalBuilder {

	final private Proposal proposal;

	private ProposalBuilder(String name) {
		proposal = new Proposal();
		proposal.setName(name);
	}

	public static ProposalBuilder create(String name) {
		return new ProposalBuilder(name);
	}

	public ProposalBuilder withOwnerId(String ownerId) {
		proposal.setOwnerId(ownerId);
		return this;
	}

	public ProposalBuilder withInsuredId(String insuredId) {
		proposal.setInsuredId(insuredId);
		return this;
	}

	public ProposalBuilder withDependents(String... dependents) {
		proposal.setDependentIds(Arrays.asList(dependents));
		return this;
	}

	public Proposal build() {
		return proposal;
	}
}
