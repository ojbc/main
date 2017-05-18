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
package org.ojbc.bundles.adapters.consentmanagement.processor;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.ojbc.bundles.adapters.consentmanagement.util.ConsentManagementAdapterTestUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-consent-management-datastore.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml"
		})
public class TestConsentXMLProcessor {
	
	@Autowired
	private ConsentXMLProcessor consentXMLProcessor;

	@Test
	public void testCreateConsentReport() throws Exception
	{
		
		Consent consent = ConsentManagementAdapterTestUtils.returnConsent(2, "b1", "n1", LocalDate.now(), "First", "M", "Last", "middle", LocalDateTime.now());
		
		consent.setConsentId(1);
		consent.setConsentDecisionTimestamp(LocalDateTime.now());
		consent.setConsentUserFirstName("consent first");
		consent.setConsentUserLastName("consent last");
		consent.setConsenterUserID("Consent user ID");
		
		Document doc = consentXMLProcessor.createConsentReport(consent);
		
		XmlUtils.printNode(doc);
		
		assertEquals("First", XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/nc30:Person/nc30:PersonName/nc30:PersonGivenName"));
		assertEquals("Last", XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/nc30:Person/nc30:PersonName/nc30:PersonSurName"));
		assertEquals("middle", XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/nc30:Person/nc30:PersonName/nc30:PersonMiddleName"));
		assertEquals(LocalDate.now().toString(), XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/nc30:Person/nc30:PersonBirthDate/nc30:Date"));
		assertEquals("M", XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/nc30:Person/jxdm51:PersonSexCode"));

		assertEquals("n1", XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/jxdm51:Booking/jxdm51:BookingSubject/jxdm51:SubjectIdentification/nc30:IdentificationID"));
		assertEquals("b1", XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/jxdm51:Booking/jxdm51:BookingAgencyRecordIdentification/nc30:IdentificationID"));
		
		assertEquals("1", XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/cdr-ext:ConsentDecision/nc30:ActivityIdentification/nc30:IdentificationID"));
		assertEquals("Consent Granted", XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/cdr-ext:ConsentDecision/cdr-ext:ConsentDecisionCode"));
		assertEquals("consent first", XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/cdr-ext:ConsentDecision/cdr-ext:ConsentDecisionRecordingEntity/nc30:EntityPerson/nc30:PersonName/nc30:PersonGivenName"));
		assertEquals("consent last", XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/cdr-ext:ConsentDecision/cdr-ext:ConsentDecisionRecordingEntity/nc30:EntityPerson/nc30:PersonName/nc30:PersonSurName"));
		assertEquals("Consent user ID", XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/cdr-ext:ConsentDecision/cdr-ext:ConsentDecisionRecordingEntity/cdr-ext:RecordingEntityUsernameText"));
		assertNotNull(XmlUtils.xPathStringSearch(doc, "/cdr-doc:ConsentDecisionReport/cdr-ext:ConsentDecision/nc30:ActivityDate/nc30:DateTime"));
	}
}
