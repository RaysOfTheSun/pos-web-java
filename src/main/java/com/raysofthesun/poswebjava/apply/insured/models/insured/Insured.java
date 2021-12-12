package com.raysofthesun.poswebjava.apply.insured.models.insured;

import com.raysofthesun.poswebjava.apply.insured.constants.InsuredRole;
import com.raysofthesun.poswebjava.apply.insured.models.person.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("insureds")
@EqualsAndHashCode(callSuper = true)
public class Insured extends Person {
	String customerId = "";

	InsuredRole role;

	InsuredIdentificationInfo identificationInfo;
}
