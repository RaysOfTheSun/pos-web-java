package com.raysofthesun.poswebjava.apply.feign_clients.agent.models;

import com.raysofthesun.poswebjava.agent.constants.CustomerRelationship;
import lombok.Data;

@Data
public class Customer {
	private String id;

	private PersonalInfo personalInfo = new PersonalInfo();

	private String agentId;

	private IncomeSource incomeSource;

	private CustomerRelationship relationshipWithCustomer;
}
