package com.raysofthesun.poswebjava.apply.feign.models;

import com.raysofthesun.poswebjava.customer.enums.CustomerRelationship;
import lombok.Data;

@Data
public class ApiCustomer {
	private String id;

	private ApiCustomerPersonalInfo personalInfo = new ApiCustomerPersonalInfo();

	private String agentId;

	private ApiCustomerIncomeSource incomeSource;

	private CustomerRelationship relationshipWithCustomer;
}
