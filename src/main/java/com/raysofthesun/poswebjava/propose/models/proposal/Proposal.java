package com.raysofthesun.poswebjava.propose.models.proposal;

import com.raysofthesun.poswebjava.propose.constants.ProposalStatus;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class Proposal {
	private String id;
	private String name;
	private String creationDate = Instant.now().toString();

	private String agentId;
	private String ownerId;
	private String insuredId;
	private List<String> dependentIds;

	private ProposalStatus status = ProposalStatus.DRAFT;
}
