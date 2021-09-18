package com.raysofthesun.poswebjava.agent.models.agent;

import com.raysofthesun.poswebjava.agent.models.agent.jobinfo.AgentJobInfo;
import com.raysofthesun.poswebjava.agent.models.person.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Agent extends Person {
	private AgentJobInfo jobInfo;

}
