package com.raysofthesun.poswebjava.agent.models.customer;

import com.raysofthesun.poswebjava.agent.models.customer.jobInfo.CustomerJobInfo;
import com.raysofthesun.poswebjava.agent.models.person.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("customers")
@EqualsAndHashCode(callSuper = true)
public class Customer extends Person {
	private CustomerJobInfo jobInfo;
}
