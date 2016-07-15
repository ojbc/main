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
import java.text.SimpleDateFormat;
import java.util.Date;

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


public class CustodySearchResultBuilderTest {
	
	private static final SimpleDateFormat SDF_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	@Test
	public void testCustodySearchResultBuilder() throws Exception{
								
		Document custodySearchResultsDoc = createNewDocument();		
		
		Element custodySearchResultsRootElement = custodySearchResultsDoc.createElementNS(OjbcNamespaceContext.NS_CUSTODY_SEARCH_RESULTS, "CustodySearchResults");
		
		custodySearchResultsRootElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_CUSTODY_SEARCH_RESULTS);
		
		custodySearchResultsDoc.appendChild(custodySearchResultsRootElement);							
		
        ClasspathXmlDataSource ds = new ClasspathXmlDataSource("DocBuilderTestFiles/Custody");
        IdentifiableDocumentWrapper documentWrapper = ds.getDocument("CustodyDetailQueryResults.xml");

		Element custodySearchResultElement =  CustodySearchResultBuilder.buildCustodySearchResultElement(custodySearchResultsDoc, documentWrapper, "0");
		
		custodySearchResultsRootElement.appendChild(custodySearchResultElement);		
		
		XmlUtils.printNode(custodySearchResultsRootElement);
		
		OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext();
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(custodySearchResultsRootElement);				
		
		XmlUtils.printNode(custodySearchResultsDoc);
		
		XmlUtils.validateInstance("ssp/Custody_Search_Results/artifacts/service_model/information_model/IEPD/xsd", 
			    "Subset/niem", "exchange_schema.xsd", custodySearchResultsDoc);		
		
		XmlTestUtils.compareDocs("src/test/resources/DocBuilderTestFiles/Custody/CustodySearchResults.xml", 
				custodySearchResultsDoc);		
	}
	
	
	@Test
	public void testGetCustodyDetail() throws Exception{
		
		Document custodyDetailDoc = XmlUtils.parseFileToDocument(
				new File("src/test/resources/DocBuilderTestFiles/Custody/CustodyDetailQueryResults.xml"));		
				
		CustodyDetail custodyDetail = CustodySearchResultBuilder.getCustodyDetail(custodyDetailDoc);
		
		String bookingSubjectId = custodyDetail.getBookingSubjectId();		
		Assert.assertEquals("abc123", bookingSubjectId);
		
		String chargeCount = custodyDetail.getChargeCount();
		// TODO FIXME
//		Assert.assertEquals("3", chargeCount);
		
		String chargeDescription = custodyDetail.getChargeDescription();
		Assert.assertEquals("Not wearing seatbelt", chargeDescription);
		
		String statuteCodeId = custodyDetail.getChargeStatuteCodeId();
		Assert.assertEquals("789", statuteCodeId);
		
		String docCreationDate = custodyDetail.getDocCreationDate();
		Assert.assertEquals("2012-12-17T09:30:47", docCreationDate);
		
		String docId = custodyDetail.getDocId();
		Assert.assertEquals("abc123", docId);		
		
		Date dBookingActivityDate = custodyDetail.getBookingActivityDate();
		
		String sBookingActivityDate = SDF_DATE_TIME.format(dBookingActivityDate);		
		Assert.assertEquals("2013-12-17T09:30:00", sBookingActivityDate);
		
		String imgLoc = custodyDetail.getImageLocation();
		//TODO FIXME
		//Assert.assertEquals("http://asco.ojbc.org/inmatelookup/image/33334444", imgLoc);
		
		String lastUpdatedDate = custodyDetail.getLastUpdatedDate();
		Assert.assertEquals("2015-09-23", lastUpdatedDate);
		
		String orgBranchName = custodyDetail.getOrganizationBranchName();
		Assert.assertEquals("Judge Judy", orgBranchName);
		
		String orgName = custodyDetail.getOrganizationName();
		Assert.assertEquals("County Court", orgName);
	}
	
	
	private static Document createNewDocument() throws ParserConfigurationException {
		
		DocumentBuilder db = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder();
		Document rDocument = db.newDocument();
		
		return rDocument;
	}

}

