package com.raysofthesun.poswebjava.apply.feign_clients.agent.models;

import com.raysofthesun.poswebjava.apply.feign_clients.agent.models.personalinfo.PersonalInfo;
import lombok.Data;

@Data
public class Customer {
	private String id;
	private String agentId;
	private JobInfo jobInfo;
	private PersonalInfo personalInfo;
}
