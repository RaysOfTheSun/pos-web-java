package com.raysofthesun.poswebjava.propose.models.prospect.personalInfo;

import com.raysofthesun.poswebjava.propose.constants.ProspectSalutation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProspectNameInfo {
	private String last;
	private String first;
	private String middle;
	private ProspectSalutation salutation;

	public ProspectNameInfo(String firstName, String lastName) {
		this.last = lastName;
		this.first = firstName;
	}
}
