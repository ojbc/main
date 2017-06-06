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
package org.ojbc.adapters.rapbackdatastore.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.camel.model.ModelCamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/spring-context.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",      
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
        "classpath:META-INF/spring/subscription-management-routes.xml"
      })
@DirtiesContext
public class TestSubscriptionModificationXMLProcessor {
	
	private static final Log log = LogFactory.getLog( TestSubscriptionModificationXMLProcessor.class );

    @Resource
    private SubscriptionModificationXMLProcessor subModificationXMLProcessor;
    
    @Resource
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
    
    @Resource  
    private DataSource dataSource;  
    
    @Resource
    private ModelCamelContext context;

    @Test
    public void testCreateOJBCsubscriptionModificationDocument() throws Exception
    {
    	assertNotNull(subscriptionSearchQueryDAO);
    	
    	Subscription subscription = subscriptionSearchQueryDAO.findSubscriptionByFbiSubscriptionId("fbiSubscriptionId_3");
    	
    	assertNotNull(subscription);
    	
    	log.info("Subscription retrieved from database: " + subscription);
    	
    	//This subscription does not have a SID or FBI ID.  Manually add one for testing
    	subscription.getSubscriptionSubjectIdentifiers().put(SubscriptionNotificationConstants.SID, "SID123");
    	subscription.getSubscriptionSubjectIdentifiers().put(SubscriptionNotificationConstants.FBI_ID, "FBI123");
    	
    	Document subscriptionModificationDoc = subModificationXMLProcessor.createOJBCsubscriptionModificationDocument(subscription);
    	
		Node subMsgNode = XmlUtils.xPathNodeSearch(subscriptionModificationDoc, "//smm:SubscriptionModificationMessage");
		assertNotNull(subMsgNode);
    	
    	XmlUtils.printNode(subscriptionModificationDoc);
    	
		Node subjectNode = XmlUtils.xPathNodeSearch(subMsgNode, "submsg-ext:Subject");
		assertNotNull(subjectNode);
		
		String sDob = XmlUtils.xPathStringSearch(subjectNode, "nc:PersonBirthDate/nc:Date");
		assertEquals("1987-10-10", sDob);
		
		Node personNameNode = XmlUtils.xPathNodeSearch(subjectNode, "nc:PersonName");	
		assertNotNull(personNameNode);				
		
		String sFirstName = XmlUtils.xPathStringSearch(personNameNode, "nc:PersonGivenName");
		assertEquals("Bart", sFirstName);
		
		String sLastName = XmlUtils.xPathStringSearch(personNameNode, "nc:PersonSurName");
		assertEquals("Simpson", sLastName);
		
		String fullName = XmlUtils.xPathStringSearch(personNameNode, "nc:PersonFullName");
		assertEquals("Bart Simpson", fullName);
		
		Node personAugNode = XmlUtils.xPathNodeSearch(subjectNode, "jxdm41:PersonAugmentation");
		assertNotNull(personAugNode);		
		
		String sid = XmlUtils.xPathStringSearch(personAugNode, "jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
		assertEquals("SID123", sid);

		String fbiId = XmlUtils.xPathStringSearch(personAugNode, "jxdm41:PersonFBIIdentification/nc:IdentificationID");
		assertEquals("FBI123", fbiId);

		NodeList emailNodeList = XmlUtils.xPathNodeListSearch(subMsgNode, "nc:ContactEmailID");
		
		List<String> retrievedEmailList = new ArrayList<String>();
		
		for(int i=0; i<emailNodeList.getLength(); i++){			
			Node iEmailNode = emailNodeList.item(i);	
			retrievedEmailList.add(iEmailNode.getTextContent());
		}		
		assertEquals(true, retrievedEmailList.contains("email107@email.com"));
		assertEquals(true, retrievedEmailList.contains("email8@email.com"));
		
		String sysName = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:SystemName");
		assertEquals("{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB", sysName);
				
		String subQualId = XmlUtils.xPathStringSearch(subMsgNode, 
				"submsg-ext:SubscriptionQualifierIdentification/nc:IdentificationID");
		assertNotNull(subQualId);
		
		Node dateRangeNode = XmlUtils.xPathNodeSearch(subMsgNode, "nc:DateRange");
		
		String startDate =  XmlUtils.xPathStringSearch(dateRangeNode, "nc:StartDate/nc:Date");
		assertEquals("2015-10-16", startDate);
		
		String endDate = XmlUtils.xPathStringSearch(dateRangeNode, "nc:EndDate/nc:Date");
		assertEquals("2016-10-19", endDate);	
		
		String ori = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:SubscribingOrganization/jxdm41:OrganizationAugmentation/jxdm41:OrganizationORIIdentification/nc:IdentificationID");
		assertEquals("TODO: get ORI here", ori);

		String federalRapSheetDisclosureIndicator = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:FederalRapSheetDisclosure/submsg-ext:FederalRapSheetDisclosureIndicator");
		assertEquals("true", federalRapSheetDisclosureIndicator);

		String federalRapSheetDisclosureAttentionDesignationText = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:FederalRapSheetDisclosure/submsg-ext:FederalRapSheetDisclosureAttentionDesignationText");
		assertEquals("Bill Padmanabhan", federalRapSheetDisclosureAttentionDesignationText);

		NodeList triggeringEventCodes =XmlUtils.xPathNodeListSearch(subMsgNode, "submsg-ext:TriggeringEvents/submsg-ext:FederalTriggeringEventCode");
		
		List<String> triggeringEventCodesList = new ArrayList<String>();
		
		for(int i=0; i<triggeringEventCodes.getLength(); i++){			
			Node triggeringEventCode = triggeringEventCodes.item(i);	
			triggeringEventCodesList.add(triggeringEventCode.getTextContent());
		}		
		assertEquals(true, triggeringEventCodesList.contains("NCIC-WARRANT-DELETION"));
		assertEquals(true, triggeringEventCodesList.contains("ARREST"));
		assertEquals(true, triggeringEventCodesList.contains("NCIC-WARRANT-ENTRY"));
		assertEquals(true, triggeringEventCodesList.contains("NCIC-WARRANT-MODIFICATION"));
    	
    }
	
}
