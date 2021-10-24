package com.raysofthesun.poswebjava.agent.models.person;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneNumber {
	private String type;
	private String number;
	private String countryCode;
}
