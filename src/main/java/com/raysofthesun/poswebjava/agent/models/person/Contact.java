package com.raysofthesun.poswebjava.agent.models.person;

import com.raysofthesun.poswebjava.agent.constants.ContactType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact {
	private String value;
	private String prefix;
	private ContactType type;
}
