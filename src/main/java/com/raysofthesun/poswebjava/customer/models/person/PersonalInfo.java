package com.raysofthesun.poswebjava.customer.models.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raysofthesun.poswebjava.customer.enums.PersonGender;
import com.raysofthesun.poswebjava.customer.enums.Salutation;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
