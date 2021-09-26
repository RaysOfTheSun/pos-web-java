package com.raysofthesun.poswebjava.propose.feign_cients.applications.models.application;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationCreationRequest {
	private String name;

	private String productType;
	private String totalPremium;
	private String paymentFrequency;

	private String policyOwnerId;
	private String primaryInsuredId;
	private List<String> dependentIds;
}
