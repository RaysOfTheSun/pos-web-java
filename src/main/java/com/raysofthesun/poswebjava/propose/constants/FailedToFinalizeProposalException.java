package com.raysofthesun.poswebjava.propose.constants;

import com.raysofthesun.poswebjava.propose.models.Proposal;

public class FailedToFinalizeProposalException extends ProposeApiException {
	public FailedToFinalizeProposalException(String proposalId) {
		super(String.format("failed to finalize proposal with id %s", proposalId));
	}
}
