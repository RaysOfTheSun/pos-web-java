package com.raysofthesun.poswebjava.propose.constants;

public class CannotFindProposalException extends ProposeApiException {
	public CannotFindProposalException(String proposalId) {
		super(String.format("Cannot find proposal with id: %s", proposalId));
	}
}
