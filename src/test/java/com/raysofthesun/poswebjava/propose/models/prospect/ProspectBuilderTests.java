package com.raysofthesun.poswebjava.propose.models.prospect;

import com.raysofthesun.poswebjava.propose.constants.ProspectGender;
import com.raysofthesun.poswebjava.propose.constants.ProspectSalutation;
import com.raysofthesun.poswebjava.propose.models.prospect.personalInfo.ProspectBirthInfo;
import com.raysofthesun.poswebjava.propose.models.prospect.personalInfo.ProspectNameInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProspectBuilder")
public class ProspectBuilderTests {

	private final String prospectLastName = "Last";
	private final String prospectFirstName = "First";
	private final ProspectSalutation prospectSalutation = ProspectSalutation.MRS;

	private final String prospectDob = "TEST_DOB";
	private final ProspectGender prospectGender = ProspectGender.FEMALE;

	@Test
	@DisplayName("should be able to create a complete prospect")
	public void shouldCreateWProspectWithCompleteInfo() {
		final Prospect prospect = ProspectBuilder.create()
				.withName(prospectFirstName, prospectLastName, prospectSalutation)
				.withBirthInfo(prospectDob, prospectGender)
				.build();

		final ProspectNameInfo prospectNameInfo = prospect.getPersonalInfo().getNameInfo();
		final ProspectBirthInfo prospectBirthInfo = prospect.getPersonalInfo().getBirthInfo();

		assertAll(
				() -> assertEquals(prospectFirstName, prospectNameInfo.getFirst()),
				() -> assertEquals(prospectLastName, prospectNameInfo.getLast()),
				() -> assertEquals(prospectGender, prospectBirthInfo.getGender()),
				() -> assertEquals(prospectDob, prospectBirthInfo.getDateOfBirth())
		);

	}
}
