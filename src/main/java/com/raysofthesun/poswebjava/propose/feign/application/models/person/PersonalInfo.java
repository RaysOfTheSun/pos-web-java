package com.raysofthesun.poswebjava.propose.feign.application.models.person;

import com.raysofthesun.poswebjava.customer2.enums.PersonGender;
import com.raysofthesun.poswebjava.customer2.enums.Salutation;
import lombok.Data;

@Data
public class PersonalInfo {
	private int age;
	private String lastName;
	private String firstName;
	private String middleName = "";
	private String dateOfBirth;
	private Measurable weight = new  Measurable();
	private Measurable height = new Measurable();
	private PersonGender gender;
	private Salutation salutation;
}
