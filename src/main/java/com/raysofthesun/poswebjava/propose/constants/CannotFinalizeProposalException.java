package com.raysofthesun.poswebjava.propose.constants;

import com.raysofthesun.poswebjava.propose.models.proposal.Proposal;

public class CannotFinalizeProposalException extends RuntimeException {
	public CannotFinalizeProposalException(Proposal proposal) {
		super(
				String.format("Cannot finalize proposal with id %s because it is either already EXPIRED or CONVERTED",
						proposal.getId()));
	}
}
