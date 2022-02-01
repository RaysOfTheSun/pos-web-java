package com.raysofthesun.poswebjava.agent.customer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raysofthesun.poswebjava.agent.customer.constants.CustomerRelationship;
import com.raysofthesun.poswebjava.agent.customer.models.person.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("customers")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer extends Person {

	@JsonIgnore
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String agentId;

	private IncomeSource incomeSource = new IncomeSource();

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private boolean deleted;

	private CustomerRelationship relationshipWithCustomer = CustomerRelationship.SELF;
}
