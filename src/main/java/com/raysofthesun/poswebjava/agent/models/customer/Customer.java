package com.raysofthesun.poswebjava.agent.models.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raysofthesun.poswebjava.agent.constants.CustomerRelationship;
import com.raysofthesun.poswebjava.agent.models.customer.jobInfo.CustomerJobInfo;
import com.raysofthesun.poswebjava.agent.models.person.Person;
import com.raysofthesun.poswebjava.agent.repositories.CustomerRepository;
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

	private CustomerJobInfo jobInfo;

	private CustomerRelationship relationshipWithCustomer = CustomerRelationship.SELF;
}
