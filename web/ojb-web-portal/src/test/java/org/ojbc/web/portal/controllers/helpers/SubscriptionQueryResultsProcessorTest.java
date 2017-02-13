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
package org.ojbc.web.portal.controllers.helpers;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.ojbc.util.xml.subscription.Subscription;
import org.w3c.dom.Document;

public class SubscriptionQueryResultsProcessorTest {
	
	@Test
	public void testParsSubQueryResults() throws Exception{
				
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
		Document sampleSubQueryResultsDoc = getSampleSubQueryResultsDoc("src/test/resources/subscriptions/SampleSubscriptionQueryResults.xml");		
		
		SubscriptionQueryResultsProcessor subQueryResultsProcessor = new SubscriptionQueryResultsProcessor();
		
		Subscription subscription = subQueryResultsProcessor.parseSubscriptionQueryResults(sampleSubQueryResultsDoc);
				
		Date dStartDate = subscription.getSubscriptionStartDate();
		String sStartDate = sdf.format(dStartDate);
		assertEquals("2014-04-01", sStartDate);
				
		Date dEndDate = subscription.getSubscriptionEndDate();
		String sEndDate = sdf.format(dEndDate);
		assertEquals("2014-05-01", sEndDate);
				
		String topic = subscription.getTopic();
		assertEquals("{http://ojbc.org/wsn/topics}:person/arrest", topic);				
				
		String sFullName = subscription.getFullName();
		assertEquals("Test Name", sFullName);
		
		Date dDob = subscription.getDateOfBirth();
    	Date birthDate = sdf.parse("1975-01-12");		
		assertEquals(0,dDob.compareTo(birthDate));
				
		List<String> emailList = subscription.getEmailList();				
		boolean hasEmail1 = emailList.contains("officer@gmail.com");
		assertEquals(true, hasEmail1);
				
		String sStateId = subscription.getStateId();
		assertEquals("A2588583", sStateId);	
		
		String systemId = subscription.getSystemId();
		assertEquals("62726", systemId);
	}
	
	@Test
	public void testParseSubQueryResultsNoSID() throws Exception{
				
		Document sampleSubQueryResultsDoc = getSampleSubQueryResultsDoc("src/test/resources/subscriptions/SampleSubscriptionQueryResultsNoSID.xml");		
		
		SubscriptionQueryResultsProcessor subQueryResultsProcessor = new SubscriptionQueryResultsProcessor();
		
		Subscription subscription = subQueryResultsProcessor.parseSubscriptionQueryResults(sampleSubQueryResultsDoc);
		
		assertNotNull(subscription);
				
	}
	
	private Document getSampleSubQueryResultsDoc(String filePath) throws Exception{
		
		File rapSheetFile = new File(filePath);
		InputStream rapsheetInStream = new FileInputStream(rapSheetFile);				
		DocumentBuilder docBuilder = getDocBuilder();		
		Document rapSheetDoc = docBuilder.parse(rapsheetInStream);	

		return rapSheetDoc;
	}
	
	private DocumentBuilder getDocBuilder() throws ParserConfigurationException{
		
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		fact.setNamespaceAware(true);
		DocumentBuilder docBuilder;
		docBuilder = fact.newDocumentBuilder();
		
		return docBuilder;
	}	

}

