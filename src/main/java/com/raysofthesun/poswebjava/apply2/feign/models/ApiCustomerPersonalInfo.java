package com.raysofthesun.poswebjava.apply2.feign.models;

import com.raysofthesun.poswebjava.customer2.enums.PersonGender;
import com.raysofthesun.poswebjava.customer2.enums.Salutation;
import lombok.Data;

@Data
public class ApiCustomerPersonalInfo {
	private String lastName;

	private String firstName;

	private String middleName = "";

	private Salutation salutation;

	private int age;

	private String dateOfBirth;

	private PersonGender gender;
}
