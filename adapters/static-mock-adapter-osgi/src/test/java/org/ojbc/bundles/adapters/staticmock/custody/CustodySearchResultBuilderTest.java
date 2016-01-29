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
package org.ojbc.bundles.adapters.staticmock.custody;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;
import net.sf.saxon.dom.DocumentBuilderFactoryImpl;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.ojbc.bundles.adapters.staticmock.StaticMockQuery;
import org.ojbc.test.util.XmlTestUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class CustodySearchResultBuilderTest {
	
	@Before
	public void init(){
		
		// TODO put these in XmlTestUtils
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLUnit.setIgnoreWhitespace(true);
	}
	
	@Test
	public void testCustodySearchResultBuilder() throws Exception{
								
		Document custodySearchResultsDoc = createNewDocument();		
		Element custodySearchResultsRootElement = custodySearchResultsDoc.createElementNS(OjbcNamespaceContext.NS_CUSTODY_SEARCH_RESULTS, "CustodySearchResults");			
		custodySearchResultsRootElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_CUSTODY_SEARCH_RESULTS);			
		custodySearchResultsDoc.appendChild(custodySearchResultsRootElement);							

		
		Document custodyDetailDoc = XmlUtils.parseFileToDocument(
				new File("src/test/resources/DocBuilderTestFiles/Custody/CustodyDetailQueryResults.xml"));
		
		Element custodySearchResultElement =  CustodySearchResultBuilder.buildCustodySearchResultElement(custodySearchResultsDoc, custodyDetailDoc, "0");
		
		custodySearchResultsRootElement.appendChild(custodySearchResultElement);		
		
		OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext();
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(custodySearchResultsRootElement);		
		
		XmlUtils.printNode(custodySearchResultsDoc);
		
		XmlTestUtils.compareDocs("src/test/resources/DocBuilderTestFiles/Custody/CustodySearchResults.xml", 
				custodySearchResultsDoc);		
	}
	
	
	private static Document createNewDocument() throws ParserConfigurationException {
		
		DocumentBuilder db = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder();
		Document rDocument = db.newDocument();
		
		return rDocument;
	}
	
	
	@Ignore
	public void getCustodyDetailTest() throws Exception{
		
		CustodySearchResultBuilder custodySearchResultBuilder = new CustodySearchResultBuilder();
		
		CustodyDetail custodyDetail = custodySearchResultBuilder.getCustodyDetail(null);
		
		String bookingSubjectId = custodyDetail.getBookingSubjectId();		
		Assert.assertEquals("TODO", bookingSubjectId);
		
		String chargeCount = custodyDetail.getChargeCount();
		Assert.assertEquals("TODO", chargeCount);
		
		String chargeDescription = custodyDetail.getChargeDescription();
		Assert.assertEquals("TODO", chargeDescription);
		
		String statuteCodeId = custodyDetail.getChargeStatuteCodeId();
		Assert.assertEquals("TODO", statuteCodeId);
		
		String docCreationDate = custodyDetail.getDocCreationDate();
		Assert.assertEquals("TODO", docCreationDate);
		
		String docId = custodyDetail.getDocId();
		Assert.assertEquals("TODO", docId);
		
		String docIdCatDesc = custodyDetail.getDocumentIdCategoryDescription();
		Assert.assertEquals("TODO", docIdCatDesc);
		
		String fingerprintDate = custodyDetail.getFingerprintDate();
		Assert.assertEquals("TODO", fingerprintDate);
		
		String imgLoc = custodyDetail.getImageLocation();
		Assert.assertEquals("TODO", imgLoc);
		
		String lastUpdatedDate = custodyDetail.getLastUpdatedDate();
		Assert.assertEquals("TODO", lastUpdatedDate);
		
		String orgBranchName = custodyDetail.getOrganizationBranchName();
		Assert.assertEquals("TODO", orgBranchName);
		
		String orgName = custodyDetail.getOrganizationName();
		Assert.assertEquals("TODO", orgName);
	}
	

	private CustodyDetail getSampleCustodyDetail(){
		
		CustodyDetail custodyDetail = new CustodyDetail();
		
		custodyDetail.setBookingSubjectId("123");
		custodyDetail.setChargeCount("5");
		custodyDetail.setChargeDescription("No Seatbelt");
		custodyDetail.setChargeStatuteCodeId("xyz");
		custodyDetail.setDocCreationDate("2016-01-29");
		custodyDetail.setDocId("789");
		custodyDetail.setDocumentIdCategoryDescription("Driving Rules");
		custodyDetail.setFingerprintDate("2016-01-29");
		custodyDetail.setImageLocation("http://fingerprints.gov/123.jpg");
		custodyDetail.setLastUpdatedDate("2016-01-29");
		custodyDetail.setOrganizationBranchName("Seatbelt branch");
		custodyDetail.setOrganizationName("Cops");
		custodyDetail.setPersonDob("1970-01-01");
		custodyDetail.setPersonGivenName("Homer");
		custodyDetail.setPersonMiddleName("Jay");
		custodyDetail.setPersonSurName("Simpson");
		custodyDetail.setPersonSex("M");
		custodyDetail.setPersonSsn("123-45-6789");
		custodyDetail.setPersonStateId("ABCD1234");
		custodyDetail.setSearchResultCategoryDescriptionText("Custody Results");
		custodyDetail.setSourceSystemNameText(StaticMockQuery.CUSTODY_SEARCH_SYSTEM_ID);
		custodyDetail.setStatuteIdCategoryDescriptionTxt("5678");
		custodyDetail.setSystemId("jklm");
		custodyDetail.setSystemName("asdf");
		
		return custodyDetail;		
	}

}

