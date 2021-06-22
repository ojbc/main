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
package org.ojbc.intermediaries.sn.subscription;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.topic.rapback.FederalTriggeringEventCode;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.model.rapback.Subscription;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@DirtiesContext
public class TestSubscriptionSearchQueryProcessor {

    private static final String TOPIC = "{http://ojbc.org/wsn/topics}:person/incident";
    static final DateTimeFormatter DATE_FORMATTER_YYYY_MM_DD = DateTimeFormat.forPattern("yyyy-MM-dd");
    private SubscriptionSearchQueryProcessor processor;

    @Before
    public void setUp() throws Exception {
        processor = new SubscriptionSearchQueryProcessor();
        processor.setMaxSubscriptionsCount(500);
    }

    @Test
    public void testSubscriptionQueryResponseBuildWithValidationDate() throws Exception {

        LinkedHashSet<String> emailAddresses = new LinkedHashSet<String>();
        emailAddresses.add("a@b.com");
        emailAddresses.add("b@c.com");

        HashMap<String, String> subjectIdentifiers = new HashMap<String, String>();
        subjectIdentifiers.put("subscriptionQualifier", "2109639");
        subjectIdentifiers.put("dateOfBirth", "1960-10-02");
        subjectIdentifiers.put(SubscriptionNotificationConstants.SID, "A123456789");
        
        Subscription subscriptionSearchResponse = returnSubscriptionSearchResponse("03/13/2013", "04/05/2014", TOPIC, "Joe", "Offender", "OJBC:IDP:OJBC:USER:admin", "ownerFirst", "ownerLast","61623",
                "{http://demostate.gov/SystemNames/1.0}SystemC", emailAddresses, subjectIdentifiers, null);

        assertNotNull(subscriptionSearchResponse);

        Document doc = processor.buildSubscriptionQueryResponseDoc(subscriptionSearchResponse);
        XmlUtils.printNode(doc);

        Node subscription = XmlUtils.xPathNodeSearch(doc, "/sqr:SubscriptionQueryResults/sqr-ext:SubscriptionQueryResult");
        assertNotNull(subscription);
        assertEquals("2013-03-13", XmlUtils.xPathStringSearch(subscription, "sqr-ext:Subscription/sqr-ext:SubscriptionValidation/sqr-ext:SubscriptionValidatedDate/nc:Date"));
        assertEquals("2014-03-13", XmlUtils.xPathStringSearch(subscription, "sqr-ext:Subscription/sqr-ext:SubscriptionValidation/sqr-ext:SubscriptionValidationDueDate/nc:Date"));
        assertEquals("2014-03-13", XmlUtils.xPathStringSearch(subscription, "sqr-ext:Subscription/sqr-ext:SubscriptionGracePeriod/sqr-ext:SubscriptionGracePeriodDateRange/nc:StartDate/nc:Date"));
        assertEquals("2014-04-12", XmlUtils.xPathStringSearch(subscription, "sqr-ext:Subscription/sqr-ext:SubscriptionGracePeriod/sqr-ext:SubscriptionGracePeriodDateRange/nc:EndDate/nc:Date"));
        assertEquals("1234567890", XmlUtils.xPathStringSearch(doc, "/sqr:SubscriptionQueryResults/jxdm41:Organization/jxdm41:OrganizationAugmentation/jxdm41:OrganizationORIIdentification/nc:IdentificationID"));
        assertEquals("Owner Agency", XmlUtils.xPathStringSearch(doc, "/sqr:SubscriptionQueryResults/jxdm41:Organization/nc:OrganizationName"));

    }

    @Test
    public void testBuildSubscriptionSearchResponseDoc() throws Exception{
        
        LinkedHashSet<String> emailAddresses = new LinkedHashSet<String>();
        emailAddresses.add("a@b.com");
        emailAddresses.add("b@c.com");
        
        HashMap<String, String> subjectIdentifiers = new HashMap<String, String>();
        subjectIdentifiers.put("subscriptionQualifier", "2109639");
        subjectIdentifiers.put("dateOfBirth", "1960-10-02");
        subjectIdentifiers.put(SubscriptionNotificationConstants.SID, "A123456789");
        
        HashMap<String, String> subscriptionProperties = new HashMap<String, String>();
        subscriptionProperties.put(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_INDICATOR, "true");
        subscriptionProperties.put(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT, "bill padmanabhan");
        subscriptionProperties.put(FederalTriggeringEventCode.ARREST.toString(), FederalTriggeringEventCode.ARREST.toString());
        subscriptionProperties.put(FederalTriggeringEventCode.DISPOSITION.toString(), FederalTriggeringEventCode.DISPOSITION.toString());

        
        Subscription subscriptionSearchResponse = returnSubscriptionSearchResponseWithNullValidation("03/13/2013", "04/05/2014", "{http://ojbc.org/wsn/topics}:person/incident",
                "Joe", "Offender", "OJBC:IDP:OJBC:USER:admin", "ownerFirst", "ownerLast", "61623", "{http://demostate.gov/SystemNames/1.0}SystemC",emailAddresses, subjectIdentifiers, subscriptionProperties,"");
        
        
        assertNotNull(subscriptionSearchResponse);
        
        Document doc = processor.buildSubscriptionQueryResponseDoc(subscriptionSearchResponse);

        XmlUtils.printNode(doc);
        
        //XmlUtils.validateInstance("ssp/Subscription_Search_Results/schema/information/Subscription_Search_Results_IEPD/xsd", 
        //      "Subset/niem", "exchange_schema.xsd", doc);
        
        NodeList subscriptions = XmlUtils.xPathNodeListSearch(doc, "/sqr:SubscriptionQueryResults/sqr-ext:SubscriptionQueryResult");
        assertNotNull(subscriptions);
        assertEquals(1, subscriptions.getLength());
        
        Node subscriptionQueryResult = subscriptions.item(0);
        assertNotNull(subscriptionQueryResult);
        
        String startDate = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/nc:ActivityDateRange/nc:StartDate/nc:Date");
        assertEquals("2013-03-13",startDate);
        
        String endDate = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/nc:ActivityDateRange/nc:EndDate/nc:Date");
        assertEquals("2014-04-05",endDate);

        String creationDate = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscriptionCreationDate/nc:Date");
        assertEquals("2013-03-13",creationDate);

        String lastUpdatedDate = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscriptionLastUpdatedDate/nc:Date");
        assertEquals("2013-03-13",lastUpdatedDate);

        String agencyCaseNumber = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscriptionRelatedCaseIdentification/nc:IdentificationID");
        assertEquals("123",agencyCaseNumber);

        String subscriptionActiveIndicator = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscriptionActiveIndicator");
        assertEquals("true",subscriptionActiveIndicator);

        String subscriptionQualifierIdentification = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscriptionQualifierIdentification");
        assertEquals("2109639",subscriptionQualifierIdentification);

        String personReference = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscriptionSubject/nc:RoleOfPersonReference/@s:ref");
        assertEquals("P001",personReference);
        
        String topicDialect = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/wsn-br:Topic/@Dialect");
        assertEquals("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete",topicDialect);
        
        String topic = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/wsn-br:Topic");
        assertEquals("{http://ojbc.org/wsn/topics}:person/incident",topic);
        
        String subscriptionOwner = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscriptionOriginator/sqr-ext:SubscriptionOriginatorIdentification/nc:IdentificationID");
        assertEquals("OJBC:IDP:OJBC:USER:admin",subscriptionOwner);
        
        assertNull(XmlUtils.xPathNodeSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscriptionValidation/sqr-ext:SubscriptionValidationDueDate/nc:Date"));
        assertNull(XmlUtils.xPathNodeSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscriptionValidation/sqr-ext:SubscriptionValidatedDate/nc:Date"));
        assertNull(XmlUtils.xPathNodeSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscriptionGracePeriod/sqr-ext:SubscriptionGracePeriodDateRange/nc:StartDate/nc:Date"));
        
        String subscribingSystemName = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:SourceSystemNameText");
        assertEquals("{http://demostate.gov/SystemNames/1.0}SystemC",subscribingSystemName);
        
        String subscriptionId = XmlUtils.xPathStringSearch(subscriptionQueryResult, "intel:SystemIdentifier/nc:IdentificationID");
        assertEquals("61623",subscriptionId);
        
        String systemName = XmlUtils.xPathStringSearch(subscriptionQueryResult, "intel:SystemIdentifier/intel:SystemName");
        assertEquals("Subscriptions",systemName);

        String subscribedEntityReference = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscribedEntity/@s:id");
        assertEquals("SE001",subscribedEntityReference);
        
        String ownerFirstName = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscribedEntity[@s:id='SE001']/nc:EntityPerson/nc:PersonName/nc:PersonGivenName");
        assertEquals("ownerFirst", ownerFirstName);

        String ownerLastName = XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:SubscribedEntity[@s:id='SE001']/nc:EntityPerson/nc:PersonName/nc:PersonSurName");
        assertEquals("ownerLast", ownerLastName);
        
        String personReferenceId = XmlUtils.xPathStringSearch(doc, "sqr:SubscriptionQueryResults/sqr-ext:Person/@s:id");
        assertEquals("P001",personReferenceId);
        
        String personBirthDate = XmlUtils.xPathStringSearch(doc, "sqr:SubscriptionQueryResults/sqr-ext:Person/nc:PersonBirthDate/nc:Date");
        assertEquals("1960-10-02",personBirthDate);
        
        String personGivenName = XmlUtils.xPathStringSearch(doc, "/sqr:SubscriptionQueryResults/sqr-ext:Person/nc:PersonName/nc:PersonGivenName");
        assertEquals("Joe",personGivenName);
        
        String personSurName = XmlUtils.xPathStringSearch(doc, "/sqr:SubscriptionQueryResults/sqr-ext:Person/nc:PersonName/nc:PersonSurName");
        assertEquals("Offender",personSurName);
        
        String sid = XmlUtils.xPathStringSearch(doc, "/sqr:SubscriptionQueryResults/sqr-ext:Person/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
        assertEquals("A123456789",sid);
        
        String email1 = XmlUtils.xPathStringSearch(doc, "/sqr:SubscriptionQueryResults/nc:ContactInformation[@s:id='S001CE001']/nc:ContactEmailID");
        assertEquals("a@b.com",email1);
        
        String email2 = XmlUtils.xPathStringSearch(doc, "/sqr:SubscriptionQueryResults/nc:ContactInformation[@s:id='S001CE002']/nc:ContactEmailID");
        assertEquals("b@c.com",email2);
        
        NodeList contactInfoReferenceNodes = XmlUtils.xPathNodeListSearch(doc, "/sqr:SubscriptionQueryResults/sqr-ext:SubscriptionContactInformationAssociation/sqr-ext:SubscriptionReference[@s:ref='S001']/following-sibling::nc:ContactInformationReference");
        assertNotNull(contactInfoReferenceNodes);
        
        assertEquals(2, contactInfoReferenceNodes.getLength());
        
        Node contactInfoReference1 = contactInfoReferenceNodes.item(0);
        Node contactInfoReference2 = contactInfoReferenceNodes.item(1);
        
        String contactInfoReferenceReference1Id = XmlUtils.xPathStringSearch(contactInfoReference1, "@s:ref");
        assertEquals("S001CE001",contactInfoReferenceReference1Id);

        String contactInfoReferenceReference2Id = XmlUtils.xPathStringSearch(contactInfoReference2, "@s:ref");
        assertEquals("S001CE002",contactInfoReferenceReference2Id);
        
        NodeList triggeringEvents = XmlUtils.xPathNodeListSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:TriggeringEvents/sqr-ext:FederalTriggeringEventCode");
        
        assertEquals(2, triggeringEvents.getLength());
        assertEquals(FederalTriggeringEventCode.ARREST.toString(), triggeringEvents.item(0).getTextContent());
        assertEquals(FederalTriggeringEventCode.DISPOSITION.toString(), triggeringEvents.item(1).getTextContent());
        
        assertEquals("true", XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:FederalRapSheetDisclosure/sqr-ext:FederalRapSheetDisclosureIndicator"));
        assertEquals("bill padmanabhan", XmlUtils.xPathStringSearch(subscriptionQueryResult, "sqr-ext:Subscription/sqr-ext:FederalRapSheetDisclosure/sqr-ext:FederalRapSheetDisclosureAttentionDesignationText"));

    }

    @Test
    public void testSubscriptionSearchResponseBuildWithValidationDate() throws Exception {

        LinkedHashSet<String> emailAddresses = new LinkedHashSet<String>();
        emailAddresses.add("a@b.com");
        emailAddresses.add("b@c.com");

        HashMap<String, String> subjectIdentifiers = new HashMap<String, String>();
        subjectIdentifiers.put("subscriptionQualifier", "2109639");
        subjectIdentifiers.put("dateOfBirth", "1960-10-02");
        subjectIdentifiers.put(SubscriptionNotificationConstants.SID, "A123456789");

        Subscription subscriptionSearchResponse = returnSubscriptionSearchResponse("03/13/2013", "04/05/2014", TOPIC, "Joe", "Offender", "OJBC:IDP:OJBC:USER:admin", "ownerFirst", "ownerLast","61623",
                "{http://demostate.gov/SystemNames/1.0}SystemC", emailAddresses, subjectIdentifiers,null);

        assertNotNull(subscriptionSearchResponse);

        List<Subscription> subscriptionSearchResponseList = new ArrayList<Subscription>();
        subscriptionSearchResponseList.add(subscriptionSearchResponse);

        Document doc = processor.buildSubscriptionSearchResponseDoc(subscriptionSearchResponseList);
        // XmlUtils.printNode(doc);

        Node subscription = XmlUtils.xPathNodeSearch(doc, "/ssr:SubscriptionSearchResults/ssr-ext:SubscriptionSearchResult[1]");
        assertNotNull(subscription);
        assertEquals("2013-03-13", XmlUtils.xPathStringSearch(subscription, "ssr-ext:Subscription/ssr-ext:SubscriptionValidation/ssr-ext:SubscriptionValidatedDate/nc:Date"));
        assertEquals("2014-03-13", XmlUtils.xPathStringSearch(subscription, "ssr-ext:Subscription/ssr-ext:SubscriptionValidation/ssr-ext:SubscriptionValidationDueDate/nc:Date"));
        assertEquals("2014-03-13", XmlUtils.xPathStringSearch(subscription, "ssr-ext:Subscription/ssr-ext:SubscriptionGracePeriod/ssr-ext:SubscriptionGracePeriodDateRange/nc:StartDate/nc:Date"));
        assertEquals("2014-04-12", XmlUtils.xPathStringSearch(subscription, "ssr-ext:Subscription/ssr-ext:SubscriptionGracePeriod/ssr-ext:SubscriptionGracePeriodDateRange/nc:EndDate/nc:Date"));

    }

    @Test
    public void testBasicSubscriptionResponseBuild() throws Exception {

        LinkedHashSet<String> emailAddresses = new LinkedHashSet<String>();
        emailAddresses.add("a@b.com");
        emailAddresses.add("b@c.com");

        HashMap<String, String> subjectIdentifiers = new HashMap<String, String>();
        subjectIdentifiers.put("subscriptionQualifier", "2109639");
        subjectIdentifiers.put("dateOfBirth", "1960-10-02");
        subjectIdentifiers.put(SubscriptionNotificationConstants.SID, "A123456789");

        Subscription subscriptionSearchResponse = returnSubscriptionSearchResponseWithNullValidation("03/13/2013", "04/05/2014", TOPIC, "Joe", "Offender", "OJBC:IDP:OJBC:USER:admin", "ownerFirst", "ownerLast", "61623",
                "{http://demostate.gov/SystemNames/1.0}SystemC", emailAddresses, subjectIdentifiers, null,"");

        subscriptionSearchResponse.setOri("ORI12345");
        
        assertNotNull(subscriptionSearchResponse);

        List<Subscription> subscriptionSearchResponseList = new ArrayList<Subscription>();
        subscriptionSearchResponseList.add(subscriptionSearchResponse);

        Document doc = processor.buildSubscriptionSearchResponseDoc(subscriptionSearchResponseList);

        XmlUtils.printNode(doc);

        // XmlUtils.validateInstance("ssp/Subscription_Search_Results/schema/information/Subscription_Search_Results_IEPD/xsd",
        // "Subset/niem", "exchange_schema.xsd", doc);

        NodeList subscriptions = XmlUtils.xPathNodeListSearch(doc, "/ssr:SubscriptionSearchResults/ssr-ext:SubscriptionSearchResult");
        assertNotNull(subscriptions);
        assertEquals(1, subscriptions.getLength());

        Node subscription = subscriptions.item(0);
        assertNotNull(subscription);
        
        String subscriptionRefId = XmlUtils.xPathStringSearch(subscription, "ssr-ext:Subscription/@s:id"); 
        assertEquals(subscriptionRefId, "S001"); 

        String startDate = XmlUtils.xPathStringSearch(subscription, "ssr-ext:Subscription/nc:ActivityDateRange/nc:StartDate/nc:Date");
        assertEquals("2013-03-13", startDate);

        String endDate = XmlUtils.xPathStringSearch(subscription, "ssr-ext:Subscription/nc:ActivityDateRange/nc:EndDate/nc:Date");
        assertEquals("2014-04-05", endDate);

        String personReference = XmlUtils.xPathStringSearch(subscription, "ssr-ext:Subscription/ssr-ext:SubscriptionSubject/nc:RoleOfPersonReference/@s:ref");
        assertEquals("P001", personReference);

        String topicDialect = XmlUtils.xPathStringSearch(subscription, "ssr-ext:Subscription/wsn-br:Topic/@Dialect");
        assertEquals("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete", topicDialect);

        String topic = XmlUtils.xPathStringSearch(subscription, "ssr-ext:Subscription/wsn-br:Topic");
        assertEquals(TOPIC, topic);

        String subscribedEntityReference = XmlUtils.xPathStringSearch(doc, "ssr:SubscriptionSearchResults/ssr-ext:SubscribedEntity/@s:id");
        assertEquals("SE001", subscribedEntityReference);

        String ownerFirstName = XmlUtils.xPathStringSearch(doc, "ssr:SubscriptionSearchResults/ssr-ext:SubscribedEntity[@s:id='SE001']/nc:EntityPerson/nc:PersonName/nc:PersonGivenName");
        assertEquals("ownerFirst", ownerFirstName);

        String ownerLastName = XmlUtils.xPathStringSearch(doc, "ssr:SubscriptionSearchResults/ssr-ext:SubscribedEntity[@s:id='SE001']/nc:EntityPerson/nc:PersonName/nc:PersonSurName");
        assertEquals("ownerLast", ownerLastName);

        String subscriptionOwner = XmlUtils.xPathStringSearch(subscription, "ssr-ext:Subscription/ssr-ext:SubscriptionOriginator/ssr-ext:SubscriptionOriginatorIdentification/nc:IdentificationID");
        assertEquals("OJBC:IDP:OJBC:USER:admin", subscriptionOwner);

        assertNull(XmlUtils.xPathNodeSearch(subscription, "ssr-ext:Subscription/ssr-ext:SubscriptionValidation/ssr-ext:SubscriptionValidationDueDate/nc:Date"));
        assertNull(XmlUtils.xPathNodeSearch(subscription, "ssr-ext:Subscription/ssr-ext:SubscriptionValidation/ssr-ext:SubscriptionValidatedDate/nc:Date"));
        assertNull(XmlUtils.xPathNodeSearch(subscription, "ssr-ext:Subscription/ssr-ext:SubscriptionGracePeriod/ssr-ext:SubscriptionGracePeriodDateRange/nc:StartDate/nc:Date"));

        String subscribingSystemName = XmlUtils.xPathStringSearch(subscription, "ssr-ext:SourceSystemNameText");
        assertEquals("{http://demostate.gov/SystemNames/1.0}SystemC", subscribingSystemName);

        String subscriptionId = XmlUtils.xPathStringSearch(subscription, "intel:SystemIdentifier/nc:IdentificationID");
        assertEquals("61623", subscriptionId);

        String systemName = XmlUtils.xPathStringSearch(subscription, "intel:SystemIdentifier/intel:SystemName");
        assertEquals("Subscriptions", systemName);

        String personReferenceId = XmlUtils.xPathStringSearch(doc, "ssr:SubscriptionSearchResults/ssr-ext:Person/@s:id");
        assertEquals("P001", personReferenceId);

        String personBirthDate = XmlUtils.xPathStringSearch(doc, "ssr:SubscriptionSearchResults/ssr-ext:Person/nc:PersonBirthDate/nc:Date");
        assertEquals("1960-10-02", personBirthDate);

        String personGivenName = XmlUtils.xPathStringSearch(doc, "/ssr:SubscriptionSearchResults/ssr-ext:Person/nc:PersonName/nc:PersonGivenName");
        assertEquals("Joe", personGivenName);

        String personSurName = XmlUtils.xPathStringSearch(doc, "/ssr:SubscriptionSearchResults/ssr-ext:Person/nc:PersonName/nc:PersonSurName");
        assertEquals("Offender", personSurName);

        String sid = XmlUtils.xPathStringSearch(doc, "/ssr:SubscriptionSearchResults/ssr-ext:Person/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
        assertEquals("A123456789", sid);

        String email1 = XmlUtils.xPathStringSearch(doc, "/ssr:SubscriptionSearchResults/nc:ContactInformation[@s:id='S001CE001']/nc:ContactEmailID");
        assertEquals("a@b.com", email1);

        String email2 = XmlUtils.xPathStringSearch(doc, "/ssr:SubscriptionSearchResults/nc:ContactInformation[@s:id='S001CE002']/nc:ContactEmailID");
        assertEquals("b@c.com", email2);
        
        NodeList subscribedEntitySubscriptionAssociationNodes = XmlUtils
                .xPathNodeListSearch(doc,
                        "/ssr:SubscriptionSearchResults/ssr-ext:SubscribedEntitySubscriptionAssociation/ssr-ext:SubscriptionReference[@s:ref='S001']/preceding-sibling::ssr-ext:SubscribedEntityReference");
        assertNotNull(subscribedEntitySubscriptionAssociationNodes);

        assertEquals(1, subscribedEntitySubscriptionAssociationNodes.getLength());

        NodeList contactInfoReferenceNodes = XmlUtils
                .xPathNodeListSearch(doc,
                        "/ssr:SubscriptionSearchResults/ssr-ext:SubscriptionContactInformationAssociation/ssr-ext:SubscriptionReference[@s:ref='S001']/following-sibling::nc:ContactInformationReference");
        assertNotNull(contactInfoReferenceNodes);

        assertEquals(2, contactInfoReferenceNodes.getLength());

        Node contactInfoReference1 = contactInfoReferenceNodes.item(0);
        Node contactInfoReference2 = contactInfoReferenceNodes.item(1);

        String contactInfoReferenceReference1Id = XmlUtils.xPathStringSearch(contactInfoReference1, "@s:ref");
        assertEquals("S001CE001", contactInfoReferenceReference1Id);

        String contactInfoReferenceReference2Id = XmlUtils.xPathStringSearch(contactInfoReference2, "@s:ref");
        assertEquals("S001CE002", contactInfoReferenceReference2Id);
        
        NodeList fbiSubScripitonNodes = XmlUtils.xPathNodeListSearch(doc, "/ssr:SubscriptionSearchResults/ssr-ext:FBISubscription"); 
        assertNotNull(fbiSubScripitonNodes);
        assertEquals(fbiSubScripitonNodes.getLength(), 1);
        
        Node fbiSubscriptionNode = fbiSubScripitonNodes.item(0);
        String fbiSubscriptionRefId = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "@s:id");
        assertEquals(fbiSubscriptionRefId, "FBI001");
        
        String fbiSubStartDate = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "nc:ActivityDateRange/nc:StartDate/nc:Date");
        assertEquals("2013-03-13", fbiSubStartDate);

        String fbiSubEndDate = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "nc:ActivityDateRange/nc:EndDate/nc:Date");
        assertEquals("2014-03-13", fbiSubEndDate);
        
        String fbiSubId = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "ssr-ext:SubscriptionFBIIdentification/nc:IdentificationID");
        assertEquals(fbiSubId, "fbiId1");

        String criminalSubscriptionReasonCode = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "ssr-ext:CriminalSubscriptionReasonCode");
        assertEquals(criminalSubscriptionReasonCode, "CI");
        
        String rapBackSubscriptionTermCode = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "ssr-ext:RapBackSubscriptionTermCode");
        assertEquals(rapBackSubscriptionTermCode, "2");
        
        String rapBackActivityNotificationFormatCode = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "ssr-ext:RapBackActivityNotificationFormatCode");
        assertEquals(rapBackActivityNotificationFormatCode, "1");
        
        String rapBackInStateOptOutIndicator = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "ssr-ext:RapBackInStateOptOutIndicator");
        assertEquals(rapBackInStateOptOutIndicator, "true");

    }

    private Subscription returnSubscriptionSearchResponseWithNullValidation(String startDate, String endDate, String topic, String firstName, String lastName, String subscriptionOwner, String subscriptionOwnerFirstName, String subscriptionOwnerLastName,
            String subscriptionIdentifier, String subscribingSystemIdentifier, LinkedHashSet<String> emailAddresses, HashMap<String, String> subscriptionSubjectIdentifiers, HashMap<String, String> subscriptionProperties, String agencyCaseNumber) {

        Subscription subscriptionSearchResponse = new Subscription();

        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");

        DateTime startDateDate = formatter.parseDateTime(startDate);
        DateTime endDateDate = formatter.parseDateTime(endDate);

        subscriptionSearchResponse.setDateOfBirth(subscriptionSubjectIdentifiers.get("dateOfBirth"));
        subscriptionSearchResponse.setStartDate(startDateDate);
        subscriptionSearchResponse.setEndDate(endDateDate);
        subscriptionSearchResponse.setTopic(topic);
        subscriptionSearchResponse.setPersonFirstName(firstName);
        subscriptionSearchResponse.setPersonLastName(lastName);
        subscriptionSearchResponse.setSubscriptionOwner(subscriptionOwner);
        subscriptionSearchResponse.setSubscriptionOwnerFirstName(subscriptionOwnerFirstName);
        subscriptionSearchResponse.setSubscriptionOwnerLastName(subscriptionOwnerLastName);
        subscriptionSearchResponse.setSubscriptionOwnerEmailAddress("");;
        subscriptionSearchResponse.setSubscriptionOwnerLastName(subscriptionOwnerLastName);
        subscriptionSearchResponse.setSubscriptionIdentifier(subscriptionIdentifier);
        subscriptionSearchResponse.setSubscribingSystemIdentifier(subscribingSystemIdentifier);
        subscriptionSearchResponse.setEmailAddressesToNotify(emailAddresses);
        subscriptionSearchResponse.setSubscriptionSubjectIdentifiers(subscriptionSubjectIdentifiers);
        subscriptionSearchResponse.setSubscriptionProperties(subscriptionProperties);
        subscriptionSearchResponse.setAgencyCaseNumber("123");
        subscriptionSearchResponse.setActive(true);
        subscriptionSearchResponse.setCreationDate(startDateDate);
        subscriptionSearchResponse.setLastUpdatedDate(startDateDate);

        
		FbiRapbackSubscription fbiRapbackSubscription = new FbiRapbackSubscription();
		fbiRapbackSubscription.setFbiSubscriptionId("fbiId1");
		fbiRapbackSubscription.setRapbackCategory("CI");
		fbiRapbackSubscription.setSubscriptionTerm("2");
		DateTime rapbackExpirationDate = subscriptionSearchResponse.getStartDate().plusDays(365);
		fbiRapbackSubscription.setRapbackExpirationDate(LocalDate.of(rapbackExpirationDate.getYear(), rapbackExpirationDate.getMonthOfYear(), rapbackExpirationDate.getDayOfMonth()));
		DateTime rapbackTermDate = subscriptionSearchResponse.getStartDate().plusDays(365);
		fbiRapbackSubscription.setRapbackTermDate(LocalDate.of(rapbackTermDate.getYear(), rapbackTermDate.getMonthOfYear(), rapbackTermDate.getDayOfMonth()));
		fbiRapbackSubscription.setRapbackStartDate(LocalDate.of(subscriptionSearchResponse.getStartDate().getYear(), 
				subscriptionSearchResponse.getStartDate().getMonthOfYear(), 
				subscriptionSearchResponse.getStartDate().getDayOfMonth()));
		fbiRapbackSubscription.setRapbackOptOutInState(true);
		fbiRapbackSubscription.setRapbackActivityNotificationFormat("1");
		fbiRapbackSubscription.setUcn("074644NG0");
		subscriptionSearchResponse.setFbiRapbackSubscription(fbiRapbackSubscription);

        return subscriptionSearchResponse;
    }

    private Subscription returnSubscriptionSearchResponse(String startDate, String endDate, String topic, String firstName, String lastName, String subscriptionOwner, String subscriptionOwnerFirstName,String subscriptionOwnerLastName,
            String subscriptionIdentifier, String subscribingSystemIdentifier, LinkedHashSet<String> emailAddresses, HashMap<String, String> subscriptionSubjectIdentifiers, HashMap<String, String> subscriptionProperties) {

        Subscription subscription = returnSubscriptionSearchResponseWithNullValidation(startDate, endDate, topic, firstName, lastName, subscriptionOwner, subscriptionOwnerFirstName, subscriptionOwnerLastName, subscriptionIdentifier, subscribingSystemIdentifier, emailAddresses, subscriptionSubjectIdentifiers, subscriptionProperties,"");

        DateTime startDateDate = subscription.getStartDate();
        DateTime validationDueDate = startDateDate.plusDays(365);
        subscription.setValidationDueDate(validationDueDate);
        subscription.setGracePeriod(new Interval(validationDueDate, validationDueDate.plusDays(30)));
        subscription.setLastValidationDate(startDateDate);
        subscription.setActive(true);
        subscription.setCreationDate(startDateDate);
        subscription.setLastUpdatedDate(startDateDate);
        subscription.setSubscriptionOwnerEmailAddress("owner@ojbc.local");
        subscription.setOri("1234567890");
        subscription.setAgencyName("Owner Agency");
        
		FbiRapbackSubscription fbiRapbackSubscription = new FbiRapbackSubscription();
		fbiRapbackSubscription.setFbiSubscriptionId("fbiId1");
		fbiRapbackSubscription.setRapbackCategory("CI");
		fbiRapbackSubscription.setSubscriptionTerm("1");
		DateTime rapbackExpirationDate = subscription.getStartDate().plusDays(365);
		fbiRapbackSubscription.setRapbackExpirationDate(LocalDate.of(rapbackExpirationDate.getYear(), rapbackExpirationDate.getMonthOfYear(), rapbackExpirationDate.getDayOfMonth()));
		DateTime rapbackTermDate = subscription.getStartDate().plusDays(365);
		fbiRapbackSubscription.setRapbackTermDate(LocalDate.of(rapbackTermDate.getYear(), rapbackTermDate.getMonthOfYear(), rapbackTermDate.getDayOfMonth()));
		fbiRapbackSubscription.setRapbackStartDate(LocalDate.of(subscription.getStartDate().getYear(), 
				subscription.getStartDate().getMonthOfYear(), 
				subscription.getStartDate().getDayOfMonth()));
		fbiRapbackSubscription.setRapbackOptOutInState(true);
		fbiRapbackSubscription.setRapbackActivityNotificationFormat("1");
		fbiRapbackSubscription.setUcn("074644NG0");
		subscription.setFbiRapbackSubscription(fbiRapbackSubscription);
		return subscription;

    }

}
