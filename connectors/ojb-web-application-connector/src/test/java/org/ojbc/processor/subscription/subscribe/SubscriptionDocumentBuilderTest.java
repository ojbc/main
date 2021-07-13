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
package org.ojbc.processor.subscription.subscribe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.util.xml.subscription.SubscriptionNotificationDocumentBuilderUtils;
import org.ojbc.util.xml.subscription.Unsubscription;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/spring-beans-ojb-web-application-connector-context.xml"})
@ActiveProfiles(profiles={"person-search", "incident-search", "vehicle-search", "firearms-search","person-vehicle-to-incident-search", 
		"warrants-query", "criminal-history-query", "firearms-query","incident-report-query", 
		"professional-license-query", "rapback-search", "arrest-search", "initial-results-query", "identification-results-modification", 
		"person-to-court-case-search" ,"cannabis-license-query", "wildlife-license-query", "court-case-query","person-to-custody-search",
		"custody-query", "vehicle-crash-query", "firearms-purchase-prohibition-query",
		"subscriptions", "policy-acknowledgement", "access-control", "juvenile-query"})
public class SubscriptionDocumentBuilderTest {
		
	
	@Resource
	private Map<String, String> triggeringEventCodeTranslationMap;
	
	@Test
	public void testDocIsValid() throws Exception{
		
		Document subDoc = getSampleSubDoc();
		
		String b2RootXsdPath = "ssp/Subscription_Notification/WSDL/wsn/b-2.xsd";		
						
		// directories needed for locating different xsd's needed in validating xml doc
		String b2RootXsdDir =  "ssp/Subscription_Notification/WSDL/wsn";
		String subscribeDocXsdDirPath = "ssp/Subscription_Notification/information_model/Subscription_Document-IEPD/xsd";		
		String neimXsdDirPath = "ssp/Subscription_Notification/information_model/Subscription_Document-IEPD/xsd/NIEM_2.1";
		
		//note these 2 dirs are needed to pass test from mvn install but not when running from the ide
		String wsdlDir = "ssp/Subscription_Notification/WSDL";
		String subNotDir = "ssp/Subscription_Notification";
		
		
		List<String> xsdDirPaths = Arrays.asList(b2RootXsdDir, subscribeDocXsdDirPath, neimXsdDirPath, wsdlDir, subNotDir);		
				
		XmlUtils.validateInstanceWithAbsoluteClasspaths(b2RootXsdPath, xsdDirPaths, subDoc);
	}
	
	

	@Test
	public void testSubMessageProcessor() throws Exception {
		
		Document subDoc = getSampleSubDoc();								
					
		Node rootSubscribeNode = XmlUtils.xPathNodeSearch(subDoc, "b-2:Subscribe");		
		assertNotNull(rootSubscribeNode);
		
		String addAddress = XmlUtils.xPathStringSearch(rootSubscribeNode, 
				"b-2:ConsumerReference/add:Address");
		assertEquals("http://www.ojbc.org/OJB/SubscribeNotify", addAddress);
	
	
		String topicExp = XmlUtils.xPathStringSearch(rootSubscribeNode, "b-2:Filter/b-2:TopicExpression");
		assertEquals("topics:person/arrest", topicExp);
		
		Node subMsgNode = XmlUtils.xPathNodeSearch(rootSubscribeNode, "submsg-exch:SubscriptionMessage");
		assertNotNull(subMsgNode);
		
		Node subjectNode = XmlUtils.xPathNodeSearch(subMsgNode, "submsg-ext:Subject");
		assertNotNull(subjectNode);
		
		String sDob = XmlUtils.xPathStringSearch(subjectNode, "nc:PersonBirthDate/nc:Date");
		assertEquals("2000-12-25", sDob);
		
		Node personNameNode = XmlUtils.xPathNodeSearch(subjectNode, "nc:PersonName");	
		assertNotNull(personNameNode);				
		
		String sFirstName = XmlUtils.xPathStringSearch(personNameNode, "nc:PersonGivenName");
		assertEquals("John", sFirstName);
		
		String sLastName = XmlUtils.xPathStringSearch(personNameNode, "nc:PersonSurName");
		assertEquals("Doe", sLastName);
		
		String fullName = XmlUtils.xPathStringSearch(personNameNode, "nc:PersonFullName");
		assertEquals("John Doe", fullName);
		
		Node personAugNode = XmlUtils.xPathNodeSearch(subjectNode, "jxdm41:PersonAugmentation");
		assertNotNull(personAugNode);		
		
		String sid = XmlUtils.xPathStringSearch(personAugNode, "jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
		assertEquals("99999", sid);
		
		NodeList emailNodeList = XmlUtils.xPathNodeListSearch(subMsgNode, "nc:ContactEmailID");
		
		List<String> retrievedEmailList = new ArrayList<String>();
		
		for(int i=0; i<emailNodeList.getLength(); i++){			
			Node iEmailNode = emailNodeList.item(i);	
			retrievedEmailList.add(iEmailNode.getTextContent());
		}		
		assertEquals(true, retrievedEmailList.contains("sam@hipd.gov"));
		assertEquals(true, retrievedEmailList.contains("sally@hipd.gov"));
		
		String sysName = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:SystemName");
		assertEquals("{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB", sysName);
				
		String subQualId = XmlUtils.xPathStringSearch(subMsgNode, 
				"submsg-ext:SubscriptionQualifierIdentification/nc:IdentificationID");
		assertNotNull(subQualId);
		
		Node dateRangeNode = XmlUtils.xPathNodeSearch(subMsgNode, "nc:DateRange");
		
		String startDate =  XmlUtils.xPathStringSearch(dateRangeNode, "nc:StartDate/nc:Date");
		assertEquals("2014-04-08", startDate);
		
		String endDate = XmlUtils.xPathStringSearch(dateRangeNode, "nc:EndDate/nc:Date");
		assertEquals("2015-04-08", endDate);	
		
		String systemId = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:SubscriptionIdentification/nc:IdentificationID");
		assertEquals("88888", systemId);
		
		String idSrcTxt = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:SubscriptionIdentification/nc:IdentificationSourceText");
		assertEquals("Subscriptions", idSrcTxt);

		String ori = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:SubscribingOrganization/jxdm41:OrganizationAugmentation/jxdm41:OrganizationORIIdentification/nc:IdentificationID");
		assertEquals("1234567890", ori);

		String federalRapSheetDisclosureIndicator = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:FederalRapSheetDisclosure/submsg-ext:FederalRapSheetDisclosureIndicator");
		assertEquals("true", federalRapSheetDisclosureIndicator);

		String federalRapSheetDisclosureAttentionDesignationText = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:FederalRapSheetDisclosure/submsg-ext:FederalRapSheetDisclosureAttentionDesignationText");
		assertEquals("Attention text", federalRapSheetDisclosureAttentionDesignationText);

		NodeList triggeringEventCodes =XmlUtils.xPathNodeListSearch(subMsgNode, "submsg-ext:TriggeringEvents/submsg-ext:FederalTriggeringEventCode");
		
		List<String> triggeringEventCodesList = new ArrayList<String>();
		
		for(int i=0; i<triggeringEventCodes.getLength(); i++){			
			Node triggeringEventCode = triggeringEventCodes.item(i);	
			triggeringEventCodesList.add(triggeringEventCode.getTextContent());
		}		
		assertEquals(true, triggeringEventCodesList.contains("ARREST"));
		assertEquals(true, triggeringEventCodesList.contains("DEATH"));
		
	}
	
	
	private Document getSampleSubDoc() throws Exception{
		
		Subscription subscription = new Subscription();
				
		List<String> emailList = Arrays.asList("sam@hipd.gov", "sally@hipd.gov");
		subscription.setEmailList(emailList);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
		String sDob = "2000-12-25";
		Date dDob = sdf.parse(sDob);
		subscription.setDateOfBirth(dDob);
		
		String sSubStarDate = "2014-04-08";
		Date dSubStartDate = sdf.parse(sSubStarDate);		
		subscription.setSubscriptionStartDate(dSubStartDate);
		
		String sSubEndDate = "2015-04-08";
		Date dSubEndDate = sdf.parse(sSubEndDate);
		subscription.setSubscriptionEndDate(dSubEndDate);
		
		subscription.setTopic("topics:person/arrest");
		
		subscription.setFirstName("John");
		subscription.setLastName("Doe");
		subscription.setFullName("John Doe");
		subscription.setStateId("99999");
		
		subscription.setSystemId("88888");
		
		subscription.setOri("1234567890");
		
		subscription.setFederalRapSheetDisclosureAttentionDesignationText("Attention text");
		subscription.setFederalRapSheetDisclosureIndicator(Boolean.TRUE);
		
		List<String> triggeringEvents = new ArrayList<String>();
		
		triggeringEvents.add("ARREST");
		triggeringEvents.add("DEATH");
		
		subscription.setFederalTriggeringEventCode(triggeringEvents);
		
		Document subMsgDoc = SubscriptionNotificationDocumentBuilderUtils.createSubscriptionRequest(subscription, triggeringEventCodeTranslationMap);		
		
		return subMsgDoc;		
	}
	
	@Test(expected=Exception.class)
	public void testCreateUnubscriptionRequestFailed() throws Exception
	{
		Unsubscription unsubscription = new Unsubscription();
		
		unsubscription.setDateOfBirth(LocalDate.now());
		unsubscription.setFirstName("John");
		unsubscription.setLastName("Doe");
		unsubscription.setReasonCode(SubscriptionNotificationDocumentBuilderUtils.NON_CRIMINAL_JUSTICE_EMPLOYMENT);
		unsubscription.setSid("9999");
		unsubscription.setSubscriptionId("I123");
		unsubscription.setSystemName("System Name");
		unsubscription.setTopic("topic");
		
		SubscriptionNotificationDocumentBuilderUtils.createUnubscriptionRequest(unsubscription);
	}
	
	@Test
	public void testCreateUnubscriptionRequest() throws Exception
	{
		Unsubscription unsubscription = new Unsubscription();
		
		LocalDate now = LocalDate.now();
		
		unsubscription.setDateOfBirth(now);
		unsubscription.setFirstName("John");
		unsubscription.setLastName("Doe");
		unsubscription.setReasonCode(SubscriptionNotificationDocumentBuilderUtils.NON_CRIMINAL_JUSTICE_EMPLOYMENT);
		unsubscription.setSid("9999");
		unsubscription.setFbiNumber("8888");
		unsubscription.setSystemName("System Name");
		unsubscription.setTopic("topic");
		unsubscription.setSubscriptionQualifierIdentification("12345");
		
		ArrayList<String> emailAddresses = new ArrayList<String>();
		emailAddresses.add("john@doe.com");
		unsubscription.setEmailAddresses(emailAddresses);

		
		Document unsubscriptionDoc = SubscriptionNotificationDocumentBuilderUtils.createUnubscriptionRequest(unsubscription);
		assertNotNull(unsubscriptionDoc);
		
		XmlUtils.printNode(unsubscriptionDoc);
		
		assertEquals("John",XmlUtils.xPathStringSearch(unsubscriptionDoc, "/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/nc:PersonName/nc:PersonGivenName"));
		assertEquals("Doe", XmlUtils.xPathStringSearch(unsubscriptionDoc,"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/nc:PersonName/nc:PersonSurName"));
		assertEquals(now.toString(), XmlUtils.xPathStringSearch(unsubscriptionDoc,"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/nc:PersonBirthDate/nc:Date"));
		assertEquals(SubscriptionNotificationDocumentBuilderUtils.NON_CRIMINAL_JUSTICE_EMPLOYMENT,XmlUtils.xPathStringSearch(unsubscriptionDoc,"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:CivilSubscriptionReasonCode"));
		assertEquals("9999",XmlUtils.xPathStringSearch(unsubscriptionDoc,"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID"));
		assertEquals("8888",XmlUtils.xPathStringSearch(unsubscriptionDoc,"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID"));
		assertEquals("System Name",XmlUtils.xPathStringSearch(unsubscriptionDoc,"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:SystemName"));
		assertEquals("topic",XmlUtils.xPathStringSearch(unsubscriptionDoc,"/b-2:Unsubscribe/b-2:TopicExpression"));
		assertEquals("12345",XmlUtils.xPathStringSearch(unsubscriptionDoc,"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:SubscriptionQualifierIdentification/nc:IdentificationID"));
		assertEquals("john@doe.com",XmlUtils.xPathStringSearch(unsubscriptionDoc,"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/nc:ContactEmailID"));
		
		
		
	}

}
