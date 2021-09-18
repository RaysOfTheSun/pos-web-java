package com.raysofthesun.poswebjava.agent.models.person.personalinfo;

import com.raysofthesun.poswebjava.agent.constants.PersonGender;
import lombok.Data;

@Data
public class BirthInfo {
	private int age;
	private String dateOfBirth;
	private PersonGender gender;
}
