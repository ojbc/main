package org.ojbc.web.model.person.search;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.ojbc.web.SearchFieldMetadata;

public class PersonSearchRequestTestUtils {

	public static PersonSearchRequest createPersonSearchRequestModel()
	{
		PersonSearchRequest psr = new PersonSearchRequest();
		
		psr.setPersonGivenName("Frank");
		psr.setPersonGivenNameMetaData(SearchFieldMetadata.ExactMatch);
		
		psr.setPersonMiddleName("f");
		
		psr.setPersonSurName("Smith");
		psr.setPersonSurNameMetaData(SearchFieldMetadata.ExactMatch);
		
		DateTime ageRangeStartDate = new DateTime(1990,5,30,0,0,0, 0);
		DateTime ageRangeEndDate = new DateTime(2000,5,30,0,0,0, 0);
		
		psr.setPersonDateOfBirthRangeStart(ageRangeStartDate);
		psr.setPersonDateOfBirthRangeEnd(ageRangeEndDate);
		
		psr.setPersonRaceCode("I");
		psr.setPersonSexCode("M");

		psr.setPersonHairColor("BLK");
		psr.setPersonEyeColor("BLK");
		
		
		psr.setPersonHeightTotalInchesRangeStart(Integer.valueOf(50));
		psr.setPersonHeightTotalInchesRangeEnd(Integer.valueOf(75));
		
		psr.setPersonWeight(Integer.valueOf(210));
		psr.setPersonWeightRangeStart(Integer.valueOf(200));
		psr.setPersonWeightRangeEnd(Integer.valueOf(225));
		
		psr.setPersonSocialSecurityNumber("123456789");
		psr.setPersonDriversLicenseNumber("123456789");
		psr.setPersonDriversLicenseIssuer("WI");
		psr.setPersonSID("A123456789");
		psr.setPersonFBINumber("FBI12345");
		
		List<String> sourceSystems = new ArrayList<String>();
				
		sourceSystems.add("{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Criminal_History/1.0}Submit-Person-Search---Criminal-History");
		sourceSystems.add("{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Warrants/1.0}Submit-Person-Search---Warrants");
		
		psr.setSourceSystems(sourceSystems);

		psr.setOnBehalfOf("On Behalf Of");
		psr.setPurpose("This is the purpose");
		
		return psr;

	}

	public static PersonSearchRequest createPersonSearchRequestModelAbsoluteWeightHeight()
	{
		PersonSearchRequest psr = new PersonSearchRequest();
		
		psr.setPersonGivenName("Frank");
		psr.setPersonGivenNameMetaData(SearchFieldMetadata.ExactMatch);
		
		psr.setPersonMiddleName("f");
		
		psr.setPersonSurName("Smith");
		psr.setPersonSurNameMetaData(SearchFieldMetadata.ExactMatch);
		
		psr.setPersonWeight(150);
		psr.setPersonHeightTotalInches(60);
		
		List<String> sourceSystems = new ArrayList<String>();
				
		sourceSystems.add("{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Criminal_History/1.0}Submit-Person-Search---Criminal-History");
		sourceSystems.add("{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Warrants/1.0}Submit-Person-Search---Warrants");
		
		psr.setSourceSystems(sourceSystems);

		
		return psr;

	}

}
