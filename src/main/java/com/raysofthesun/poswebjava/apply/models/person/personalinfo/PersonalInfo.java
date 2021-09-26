package com.raysofthesun.poswebjava.apply.models.person.personalinfo;

import lombok.Data;

@Data
public class PersonalInfo {
	private NameInfo nameInfo = new NameInfo();
	private BirthInfo birthInfo = new BirthInfo();
}
