package com.raysofthesun.poswebjava.apply.insured.models.person;

import com.raysofthesun.poswebjava.agent.customer.constants.PersonGender;
import com.raysofthesun.poswebjava.agent.customer.constants.Salutation;
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
