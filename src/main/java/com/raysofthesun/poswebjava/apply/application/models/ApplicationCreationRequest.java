package com.raysofthesun.poswebjava.apply.application.models;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApplicationCreationRequest {
	private String name;

	private String productType;

	private String totalPremium;

	private String policyOwnerId;

	private String primaryInsuredId;

	private List<String> dependentIds = new ArrayList<>();
}
