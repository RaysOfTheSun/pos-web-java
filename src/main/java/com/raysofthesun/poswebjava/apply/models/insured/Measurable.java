package com.raysofthesun.poswebjava.apply.models.insured;

import com.raysofthesun.poswebjava.apply.constants.MeasurementUnit;
import lombok.Data;

@Data
public class Measurable {
	private String value;
	private MeasurementUnit unit;
}
