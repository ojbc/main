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
package org.ojbc.web.portal.controllers.helpers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.xml.subscription.Subscription;
import org.w3c.dom.Document;

public class SubscriptionQueryResultsProcessorTest {
	
	@Test
	public void testParseSubQueryResults() throws Exception{
				
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
		Document sampleSubQueryResultsDoc = getSampleSubQueryResultsDoc("src/test/resources/subscriptions/SampleSubscriptionQueryResults.xml");		
		
		SubscriptionQueryResultsProcessor subQueryResultsProcessor = new SubscriptionQueryResultsProcessor();
		
		Subscription subscription = subQueryResultsProcessor.parseSubscriptionQueryResults(sampleSubQueryResultsDoc);
				
		Date dStartDate = subscription.getSubscriptionStartDate();
		String sStartDate = sdf.format(dStartDate);
		assertEquals("2014-03-12", sStartDate);
				
		Date dEndDate = subscription.getSubscriptionEndDate();
		String sEndDate = sdf.format(dEndDate);
		assertEquals("2016-03-12", sEndDate);
				
		String topic = subscription.getTopic();
		assertEquals("{http://ojbc.org/wsn/topics}:person/arrest", topic);				
				
		assertEquals("Dwight Schrute", subscription.getFullName());
		
		Date dDob = subscription.getDateOfBirth();
    	Date birthDate = sdf.parse("1975-01-12");		
		assertEquals(0,dDob.compareTo(birthDate));
				
		List<String> emailList = subscription.getEmailList();				
		boolean hasEmail1 = emailList.containsAll(Arrays.asList("subjectEmail1@gov.gov", "subjectEmail2@gov.gov", "subjectEmail3@gov.gov"));
		assertEquals(true, hasEmail1);
		
		assertThat(subscription.getOwnerEmailAddress(), is("probation_officer@gov.gov"));
				
		String sStateId = subscription.getStateId();
		assertEquals("A1099109188", sStateId);	
		assertThat(subscription.getFbiId(), is("123456789"));
		
		String systemId = subscription.getSystemId();
		assertEquals("62726", systemId);
		assertThat(subscription.getSystemName(), is("Subscriptions"));
		
		String federalRapSheetDisclosureAttentionDesignationText =  subscription.getFederalRapSheetDisclosureAttentionDesignationText();
		assertEquals("Detective George Jones", federalRapSheetDisclosureAttentionDesignationText);
		
		Boolean federalRapSheetDisclosureIndicator =  subscription.getFederalRapSheetDisclosureIndicator();
		assertThat(federalRapSheetDisclosureIndicator, is(true));
		
		assertNotNull(subscription.getFederalTriggeringEventCode());
		assertEquals(2, subscription.getFederalTriggeringEventCode().size());
		assertTrue(subscription.getFederalTriggeringEventCode().contains("DEATH"));
		assertTrue(subscription.getFederalTriggeringEventCode().contains("ARREST"));
		
		assertNotNull(subscription.getFbiRapbackSubscription());
		FbiRapbackSubscription fbiRapbackSubscription = subscription.getFbiRapbackSubscription();
		assertThat(fbiRapbackSubscription.getFbiSubscriptionId(), is("1234567"));
		assertThat(fbiRapbackSubscription.getRapbackStartDate(), is(LocalDate.of(2015, 1, 1)));
		assertThat(fbiRapbackSubscription.getRapbackExpirationDate(), is(LocalDate.of(2016, 1, 1)));
		assertThat(fbiRapbackSubscription.getRapbackCategory(), is("CI"));
		assertThat(fbiRapbackSubscription.getSubscriptionTerm(), is("5"));
		assertThat(fbiRapbackSubscription.getRapbackActivityNotificationFormat(), is("1"));
		assertThat(fbiRapbackSubscription.getRapbackOptOutInState(), is(false));
	}
	
	@Test
	public void testParseSubQueryResultsNoSID() throws Exception{
				
		Document sampleSubQueryResultsDoc = getSampleSubQueryResultsDoc("src/test/resources/subscriptions/sampleSubscriptionQueryResultsNoSID.xml");		
		
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

