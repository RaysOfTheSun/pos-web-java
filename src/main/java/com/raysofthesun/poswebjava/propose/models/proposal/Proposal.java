package com.raysofthesun.poswebjava.propose.models.proposal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raysofthesun.poswebjava.propose.constants.ProposalStatus;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class Proposal {
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String id = UUID.randomUUID().toString();

	private String name;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String creationDate = Instant.now().toString();

	@JsonIgnore
	private String agentId;
	private String ownerId;
	private String insuredId;
	private List<String> dependentIds;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ProposalStatus status = ProposalStatus.DRAFT;
}
