package com.raysofthesun.poswebjava.apply.models.person.personalinfo;

import com.raysofthesun.poswebjava.agent.constants.PersonSalutation;
import lombok.Data;

@Data
public class NameInfo {
	private String last;
	private String first;
	private String middle = "";
	private PersonSalutation salutation;
}
