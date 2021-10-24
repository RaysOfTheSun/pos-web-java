package com.raysofthesun.poswebjava.agent.models.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raysofthesun.poswebjava.agent.constants.PersonGender;
import com.raysofthesun.poswebjava.agent.constants.Salutation;
import lombok.Data;

@Data
public class PersonalInfo {
	private String lastName;

	private String firstName;

	private String middleName = "";

	private Salutation salutation;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private int age;

	private String dateOfBirth;

	private PersonGender gender;
}
