package com.raysofthesun.poswebjava.propose.feign_cients.applications.models.person.personalinfo;

import com.raysofthesun.poswebjava.agent.constants.Salutation;
import lombok.Data;

@Data
public class NameInfo {
	private String last;
	private String first;
	private String middle = "";
	private Salutation salutation;
}
