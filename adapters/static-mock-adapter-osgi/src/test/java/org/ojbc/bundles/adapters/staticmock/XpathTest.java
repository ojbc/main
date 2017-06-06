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
package org.ojbc.bundles.adapters.staticmock;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;
import org.ojbc.bundles.adapters.staticmock.StaticMockQuery.SearchValueXPaths;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;


public class XpathTest {
	
	@Test
	public void custodyDetailXpathTest() throws Exception{
		
		File sampleCustodyDetailFile = new File("src/test/resources/StaticMockQueryXpath/sample-CustodyCaseQueryResults.xml");
		
		Document sampleCustodyDetailDoc = XmlUtils.parseFileToDocument(sampleCustodyDetailFile);
		
		StaticMockQuery staticMockQuery = new StaticMockQuery();
		
		SearchValueXPaths searchXpaths = staticMockQuery.getCustodyXPaths();
		
		String age = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.ageXPath);		
		Assert.assertEquals("52", age);
		
		String dob = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.birthdateXPath);		
		Assert.assertEquals("1968-12-17", dob.trim());
		
		String ssn = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.ssnXPath);		
		Assert.assertEquals("123-45-6789", ssn.trim());
		
		String sid = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.sidXPath);		
		Assert.assertEquals("CO0120010324", sid.trim());
		
		String fbiId = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.fbiXPath);
		Assert.assertEquals("FBI45678", fbiId.trim());
		
		String dlId = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.dlXPath);
		Assert.assertEquals("DL239486", dlId.trim());
		
		String dlJurisdiction = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.dlJurisdictionXPath);
		Assert.assertEquals("DL239486", dlJurisdiction.trim());
		
		String lastName = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.lastNameXPath);
		Assert.assertEquals("Strong", lastName.trim());
		
		String middleName = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.middleNameXPath);
		Assert.assertEquals("H.", middleName.trim());
		
		String firstName = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.firstNameXPath);
		Assert.assertEquals("Otis", firstName.trim());
				
		String eyeColor = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.eyeColorCodeXPath);
		Assert.assertEquals("BLU", eyeColor.trim());
				
		String hairColor = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.hairColorCodeXPath);
		Assert.assertEquals("BLK", hairColor.trim());
				
		String race = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.raceXPath);
		Assert.assertEquals("A", race.trim());
		
		String sexCode = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.sexXPath);
		Assert.assertEquals("F", sexCode.trim());
		
		String height = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.heightXPath);
		Assert.assertEquals("110", height.trim());
		
		String weight = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.weightXPath);
		Assert.assertEquals("110", weight.trim());	
				
		String addressStreetNum = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.addressStreetNumberXPath);
		Assert.assertEquals("30", addressStreetNum);		
		
		String addressStreet = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.addressStreetNameXPath);
		Assert.assertEquals("Main Street", addressStreet);		
		
		String addressCity = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.addressCityXPath);
		Assert.assertEquals("Denton", addressCity);
		
		String state = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.addressStateXPath);
		state = state.trim();		
		Assert.assertEquals("CO", state);
		
		String zipCode = XmlUtils.xPathStringSearch(sampleCustodyDetailDoc, searchXpaths.addressZipXPath);
		Assert.assertEquals("99999", zipCode);
	}

}


