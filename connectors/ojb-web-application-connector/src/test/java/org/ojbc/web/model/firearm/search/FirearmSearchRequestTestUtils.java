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
package org.ojbc.web.model.firearm.search;

import java.util.ArrayList;
import java.util.List;

import org.ojbc.web.SearchFieldMetadata;

public class FirearmSearchRequestTestUtils {
	public static FirearmSearchRequest createFirearmSearchRequestModel(SearchFieldMetadata serialNumberMetadata) {
		FirearmSearchRequest fsr = new FirearmSearchRequest();
		
		fsr.setFirearmSerialNumber("123476576");
		fsr.setFirearmSerialNumberMetaData(serialNumberMetadata);
		fsr.setFirearmMake("Make");
		fsr.setFirearmModel("Model");
		fsr.setFirearmType("D");
		fsr.setFirearmRegistrationNumber("Registration Number");
		fsr.setFirearmCounty("County");
		fsr.setFirearmCurrentRegOnly(true);
		fsr.setOnBehalfOf("John Doe");
		fsr.setPurpose("Criminal Justice");
	
		List<String> sourceSystems = new ArrayList<String>();
		sourceSystems.add("System");
		sourceSystems.add("PD");
		fsr.setSourceSystems(sourceSystems);
		
		return fsr;
	}
	
	
}
