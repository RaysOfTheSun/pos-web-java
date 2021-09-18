package com.raysofthesun.poswebjava.propose.models.prospect;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raysofthesun.poswebjava.propose.constants.ProspectRelationship;
import com.raysofthesun.poswebjava.propose.models.prospect.personalInfo.ProspectPersonalInfo;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("prospects")
public class Prospect {
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String id;
	private ProspectPersonalInfo personalInfo = new ProspectPersonalInfo();
	private ProspectRelationship relationshipWithPrimaryProspect = ProspectRelationship.SELF;
}
