package com.raysofthesun.poswebjava.apply.insureds.models.core.person;

import com.raysofthesun.poswebjava.apply.insureds.enums.MeasurementUnit;
import lombok.Data;

@Data
public class Measurable {
	private String value;
	private MeasurementUnit unit;
}
