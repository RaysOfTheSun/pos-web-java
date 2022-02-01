package com.raysofthesun.poswebjava.agent.agent.models;

import com.raysofthesun.poswebjava.agent.customer.models.person.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("agents")
@EqualsAndHashCode(callSuper = true)
public class Agent extends Person {
	private AgentJobInfo jobInfo;
}