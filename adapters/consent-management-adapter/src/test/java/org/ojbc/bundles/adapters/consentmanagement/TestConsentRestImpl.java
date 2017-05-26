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
package org.ojbc.bundles.adapters.consentmanagement;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.bundles.adapters.consentmanagement.dao.ConsentManagementDAOImpl;
import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.ojbc.bundles.adapters.consentmanagement.util.ConsentManagementAdapterTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

@UseAdviceWith	// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/test-beans.xml",
		"classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-consent-management-datastore.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml"
		})		
@DirtiesContext
public class TestConsentRestImpl {

	private Logger log = Logger.getLogger(TestConsentRestImpl.class.getName());
	
    @Resource
    private ModelCamelContext context;

    @Resource
    private RestTemplate restTemplate;
    
	@Autowired
	private ConsentManagementDAOImpl consentManagementDAOImpl;
    
	@Before
	public void setUp() throws Exception {
		
    	context.start();
	}
    
	@Test
	public void testConsent() throws Exception
	{
		final String uri = "https://localhost:9898/consentService/consent";
		
		Consent consent = ConsentManagementAdapterTestUtils.returnConsent(null, "B11111", "N2222", LocalDate.parse("1974-03-01"), "First", "M", "Last", "Middle", LocalDateTime.now());
		Integer consentPk = consentManagementDAOImpl.saveConsentDecision(consent);

		consent.setConsentId(consentPk);
		consent.setConsenterUserID("user ID consent");
		consent.setConsentUserFirstName("userFirstName");
		consent.setConsentUserLastName("userLastName");
		consent.setConsentDocumentControlNumber("control");
		consent.setConsentDecisionTypeID(1);
		
		restTemplate.postForLocation(uri, consent);
		
		Consent updatedConsent = consentManagementDAOImpl.returnConsentRecordfromId(consentPk);

		assertEquals(1, updatedConsent.getConsentDecisionTypeID().intValue());
		assertEquals("user ID consent", updatedConsent.getConsenterUserID());
		assertEquals("userFirstName", updatedConsent.getConsentUserFirstName());
		assertEquals("userLastName", updatedConsent.getConsentUserLastName());
		assertEquals("control", updatedConsent.getConsentDocumentControlNumber());
		assertNotNull(updatedConsent.getConsentDecisionTimestamp());
	}
	
	@Test
	public void testSearch() throws Exception
	{
		Consent consent = ConsentManagementAdapterTestUtils.returnConsent(null, "B1234", "N1234", LocalDate.parse("1975-02-01"), "TestFirst", "F", "TestLast", "TestMiddle", LocalDateTime.now());
		consentManagementDAOImpl.saveConsentDecision(consent);

		Consent consent1 = ConsentManagementAdapterTestUtils.returnConsent(null, "B2345", "N234", LocalDate.parse("1974-03-01"), "TestFirst1", "M", "TestLast1", "TestMiddle1", LocalDateTime.now());
		consentManagementDAOImpl.saveConsentDecision(consent1);

		
		final String uri = "https://localhost:9898/consentService/search";
		
		Consent[] response = restTemplate.getForObject(uri, Consent[].class);
		
		if (response != null)
		{	
			log.info(response.toString());
		}	
		
		Consent returnRecord1 = response[0];
		assertEquals("B1234", returnRecord1.getBookingNumber());
		assertEquals("N1234", returnRecord1.getNameNumber());
		assertEquals("1975-02-01", returnRecord1.getPersonDOBString());
		assertEquals("TestFirst", returnRecord1.getPersonFirstName());
		assertEquals("TestMiddle", returnRecord1.getPersonMiddleName());
		assertEquals("TestLast", returnRecord1.getPersonLastName());
		assertEquals("F", returnRecord1.getPersonGender());
		
		Consent returnRecord2 = response[1];
		assertEquals("B2345", returnRecord2.getBookingNumber());
		assertEquals("N234", returnRecord2.getNameNumber());
		assertEquals("1974-03-01", returnRecord2.getPersonDOBString());
		assertEquals("TestFirst1", returnRecord2.getPersonFirstName());
		assertEquals("TestMiddle1", returnRecord2.getPersonMiddleName());
		assertEquals("TestLast1", returnRecord2.getPersonLastName());
		assertEquals("M", returnRecord2.getPersonGender());

	}

	
}
