package com.raysofthesun.poswebjava.propose.feign.application.models.person;

import com.raysofthesun.poswebjava.apply.insured.constants.MeasurementUnit;
import lombok.Data;

@Data
public class Measurable {
	private String value;
	private MeasurementUnit unit;
}