package com.raysofthesun.poswebjava.apply2.insureds.models.core.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class Person {
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String id = UUID.randomUUID().toString();

	private PersonalInfo personalInfo = new PersonalInfo();
}