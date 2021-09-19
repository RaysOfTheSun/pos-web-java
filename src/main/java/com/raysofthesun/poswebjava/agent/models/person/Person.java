package com.raysofthesun.poswebjava.agent.models.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raysofthesun.poswebjava.agent.models.person.personalinfo.PersonalInfo;
import lombok.Data;

import java.util.UUID;

@Data
public class Person {
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String id = UUID.randomUUID().toString();

	private PersonalInfo personalInfo;
}
