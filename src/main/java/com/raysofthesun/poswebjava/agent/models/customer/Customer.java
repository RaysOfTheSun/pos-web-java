package com.raysofthesun.poswebjava.agent.models.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raysofthesun.poswebjava.agent.constants.CustomerRelationship;
import com.raysofthesun.poswebjava.agent.models.person.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("customers")
@EqualsAndHashCode(callSuper = true)
public class Customer extends Person {

	@JsonIgnore
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String agentId;

	private IncomeSource incomeSource;

	private CustomerRelationship relationshipWithCustomer = CustomerRelationship.SELF;
}
