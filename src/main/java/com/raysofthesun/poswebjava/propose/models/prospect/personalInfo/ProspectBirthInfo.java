package com.raysofthesun.poswebjava.propose.models.prospect.personalInfo;

import com.raysofthesun.poswebjava.propose.constants.ProspectGender;
import lombok.Data;

@Data
public class ProspectBirthInfo {
	private int age;
	private String dateOfBirth = "";
	private ProspectGender gender = ProspectGender.MALE;
}
