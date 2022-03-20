package com.raysofthesun.poswebjava.apply.insureds.models.core.insured;

import com.raysofthesun.poswebjava.apply.insureds.enums.InsuredRole;
import com.raysofthesun.poswebjava.apply.insureds.models.core.person.Person;
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
