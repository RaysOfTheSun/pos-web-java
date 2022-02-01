package com.raysofthesun.poswebjava.agent.customer.models.person;

import com.raysofthesun.poswebjava.agent.customer.constants.ContactType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
	private String value;
	private String prefix;
	private ContactType type;
}
