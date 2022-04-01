package com.raysofthesun.poswebjava.apply.insureds.models.core.person;

import com.raysofthesun.poswebjava.customer.enums.PersonGender;
import com.raysofthesun.poswebjava.customer.enums.Salutation;
import lombok.Data;

@Data
public class PersonalInfo {
	private int age;
	private String lastName;
	private String firstName;
	private String middleName = "";
	private String dateOfBirth;
	private Measurable weight = new Measurable();
	private Measurable height = new Measurable();
	private PersonGender gender;
	private Salutation salutation;
}