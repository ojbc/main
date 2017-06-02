/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
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
