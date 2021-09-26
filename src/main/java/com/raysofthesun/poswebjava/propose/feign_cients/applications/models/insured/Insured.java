package com.raysofthesun.poswebjava.propose.feign_cients.applications.models.insured;

import com.raysofthesun.poswebjava.apply.constants.InsuredRole;
import com.raysofthesun.poswebjava.propose.feign_cients.applications.models.person.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("insureds")
@EqualsAndHashCode(callSuper = true)
public class Insured extends Person {
	InsuredRole role;
	String customerId;

	InsuredPhysicalInfo physicalInfo;
	InsuredIdentificationInfo identificationInfo;
}
