package com.raysofthesun.poswebjava.apply2.feign.models;

import com.raysofthesun.poswebjava.customer.constants.CustomerRelationship;
import lombok.Data;

@Data
public class ApiCustomer {
	private String id;

	private ApiCustomerPersonalInfo personalInfo = new ApiCustomerPersonalInfo();

	private String agentId;

	private ApiCustomerIncomeSource incomeSource;

	private CustomerRelationship relationshipWithCustomer;
}
