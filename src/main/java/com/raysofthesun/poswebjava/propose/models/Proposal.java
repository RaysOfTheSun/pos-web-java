package com.raysofthesun.poswebjava.propose.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raysofthesun.poswebjava.propose.constants.ProposalStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Proposal {
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String id = UUID.randomUUID().toString();

	private String totalPremium = BigDecimal.valueOf(10000).toPlainString();

	private String name;

	private String type;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String creationDate = Instant.now().toString();

	@JsonIgnore
	private String agentId;

	private String ownerId;

	private String insuredId;

	private List<String> dependentIds = new ArrayList<>();

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ProposalStatus status = ProposalStatus.DRAFT;
}
