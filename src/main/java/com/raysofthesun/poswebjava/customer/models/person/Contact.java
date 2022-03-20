package com.raysofthesun.poswebjava.customer.models.person;

import com.raysofthesun.poswebjava.customer.enums.ContactType;
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
