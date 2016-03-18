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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.bundles.adapters.staticmock.courtcase;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;
import net.sf.saxon.dom.DocumentBuilderFactoryImpl;

import org.junit.Test;
import org.ojbc.bundles.adapters.staticmock.ClasspathXmlDataSource;
import org.ojbc.bundles.adapters.staticmock.IdentifiableDocumentWrapper;
import org.ojbc.test.util.XmlTestUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CourtCaseSearchResultBuilderTest {
	
	
	@Test
	public void testCourtCaseSearchResultBuilder() throws Exception{
		
        ClasspathXmlDataSource ds = new ClasspathXmlDataSource("DocBuilderTestFiles/CourtCase");
        
        IdentifiableDocumentWrapper documentWrapper = ds.getDocument("CourtCaseDetailQueryResults.xml");
        
		Document courtCaseSearchResultsDoc = createNewDocument();
		
		Element courtCaseSearchResultsRootElement = courtCaseSearchResultsDoc.createElementNS(OjbcNamespaceContext.NS_COURT_CASE_SEARCH_RESULTS, "CourtCaseSearchResults");
		
		courtCaseSearchResultsRootElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_COURT_CASE_SEARCH_RESULTS);
		
		courtCaseSearchResultsDoc.appendChild(courtCaseSearchResultsRootElement);
		
		Element courtCaseSearchResultElement = CourtCaseSearchResultBuilder.buildCourtCaseSearchResultElement(
				courtCaseSearchResultsDoc, documentWrapper, "1");
		
		courtCaseSearchResultsRootElement.appendChild(courtCaseSearchResultElement);
		
		OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext();
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(courtCaseSearchResultsRootElement);		
				
        XmlUtils.validateInstance("ssp/Court_Case_Search_Results/artifacts/service_model/information_model/IEPD/xsd", 
        		"Subset/niem", "exchange_schema.xsd", courtCaseSearchResultsDoc);
				
		XmlTestUtils.compareDocs("src/test/resources/DocBuilderTestFiles/CourtCase/CourtCaseSearchResults.xml", courtCaseSearchResultsDoc);	
	}
	
	@Test
	public void testGetCourtCaseDetail() throws Exception{
			
		Document courtCaseDetailDoc = XmlUtils.parseFileToDocument(
				new File("src/test/resources/DocBuilderTestFiles/CourtCase/CourtCaseDetailQueryResults.xml"));
				
		IdentifiableDocumentWrapper courtCaseDetailWrapper = new IdentifiableDocumentWrapper(courtCaseDetailDoc, "MockFileName.xml");
		
		CourtCaseDetail courtCaseDetail = CourtCaseSearchResultBuilder.getCourtCaseDetail(courtCaseDetailWrapper);
		
		String docketId = courtCaseDetail.getCaseDocketID();
		Assert.assertEquals("567", docketId);
		
		String caseGenCatTxt = courtCaseDetail.getCaseGeneralCategoryText();
		Assert.assertEquals("seat belt", caseGenCatTxt);
		
		String sOtherInfoCatDescTxt = courtCaseDetail.getCaseOtherInfoIdCatDescTxt();
		Assert.assertEquals("turn signal", sOtherInfoCatDescTxt);
		
		String sTrackingId = courtCaseDetail.getCaseTrackingID();
		Assert.assertEquals("123 tracker", sTrackingId);
		
		String sDriverLicenseId = courtCaseDetail.getDriverLicenseId();
		Assert.assertEquals("567890", sDriverLicenseId);
		
		String driverLicSource = courtCaseDetail.getDriverLicenseIdSourceTxt();
		Assert.assertEquals("FL", driverLicSource);
		
		String sJurisdictionTxt = courtCaseDetail.getJurisdictionText();
		Assert.assertEquals("The Court", sJurisdictionTxt);
		
		String sLastUpdatedDate = courtCaseDetail.getLastUpdatedDate();
		Assert.assertEquals("2015-09-23", sLastUpdatedDate);
		
		String sOrgBranchName = courtCaseDetail.getOrganizationBranchName();
		Assert.assertEquals("DOJ", sOrgBranchName);
		
		String sOrgName = courtCaseDetail.getOrganizationName();
		Assert.assertEquals("County Court", sOrgName);
		
		String sDob = courtCaseDetail.getPersonBirthDate();
		Assert.assertEquals("2001-12-17", sDob);
		
		String sEyeColor = courtCaseDetail.getPersonEyeColor();
		Assert.assertEquals("BLK", sEyeColor);
		
		String fbiId = courtCaseDetail.getPersonFBIId();
		Assert.assertEquals("4320", fbiId);
		
		String personGivenName = courtCaseDetail.getPersonGivenName();
		Assert.assertEquals("Homer", personGivenName);
		
		String middleName = courtCaseDetail.getPersonMiddleName();
		Assert.assertEquals("Jay", middleName);
		
		String surName = courtCaseDetail.getPersonSurName();
		Assert.assertEquals("Simpson", surName);
		
		String hairColor = courtCaseDetail.getPersonHairColor();
		Assert.assertEquals("BLD", hairColor);
		
		String height = courtCaseDetail.getPersonHeight();
		Assert.assertEquals("67", height);
		
		String raceCode = courtCaseDetail.getPersonRaceCode();
		Assert.assertEquals("A", raceCode);
		
		String personSexCode = courtCaseDetail.getPersonSexCode();
		Assert.assertEquals("M", personSexCode);
		
		String personSid = courtCaseDetail.getPersonSid();
		Assert.assertEquals("abcd5678", personSid);
		
		String weight = courtCaseDetail.getPersonWeight();
		Assert.assertEquals("144", weight);
		
		String srchResCatTxt = courtCaseDetail.getSearchResultCategoryText();
		Assert.assertEquals("Court Case", srchResCatTxt);
				
		String sysId = courtCaseDetail.getSystemId();
		Assert.assertEquals("MockFileName.xml", sysId);
		
		String personRecId = courtCaseDetail.getPersonRecId();
		Assert.assertEquals("5789989", personRecId);
		
		String sysName = courtCaseDetail.getSystemName();
		Assert.assertEquals("Court DB", sysName);
	}
	
	
	private static Document createNewDocument() throws ParserConfigurationException {
		
		DocumentBuilder db = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder();
		Document rDocument = db.newDocument();
		
		return rDocument;
	}

}
