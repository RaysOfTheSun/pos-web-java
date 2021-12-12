package com.raysofthesun.poswebjava.propose.constants;

public class FailedToFinalizeProposalException extends ProposeApiException {
	public FailedToFinalizeProposalException(String proposalId) {
		super(String.format("failed to finalize proposal with id %s", proposalId));
	}
}
