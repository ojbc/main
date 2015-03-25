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
