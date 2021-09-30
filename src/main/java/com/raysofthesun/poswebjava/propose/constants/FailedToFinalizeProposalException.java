package com.raysofthesun.poswebjava.propose.constants;

import com.raysofthesun.poswebjava.propose.models.Proposal;

public class FailedToFinalizeProposalException extends ProposeApiException {
	public FailedToFinalizeProposalException(Proposal proposal) {
		super(String.format("failed to finalize proposal with id %s", proposal.getId()));
	}
}
