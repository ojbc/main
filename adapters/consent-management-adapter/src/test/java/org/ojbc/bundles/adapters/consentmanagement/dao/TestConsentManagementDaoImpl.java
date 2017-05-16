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
package org.ojbc.bundles.adapters.consentmanagement.dao;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-consent-management-datastore.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml"
		})
@DirtiesContext
public class TestConsentManagementDaoImpl {

	private static final Log log = LogFactory.getLog(TestConsentManagementDaoImpl.class);
	
    @Resource  
    private DataSource dataSource;  
	
	@Autowired
	private ConsentManagementDAOImpl consentManagementDAOImpl;
	
	@Before
	public void setUp() throws Exception {
		Assert.assertNotNull(consentManagementDAOImpl);
		Assert.assertNotNull(dataSource);
		
		consentManagementDAOImpl.deleteAllConsentRecords();
	}
	
	@Test
	public void testReturnConsentRecordsFromLast24hours()
	{
		Consent consent = returnConsent(null, "B1234", "N1234", LocalDate.parse("1975-02-01"), "TestFirst", "M", "TestLast", "TestMiddle", LocalDateTime.now().minusHours(25));
		
		consentManagementDAOImpl.saveConsentDecision(consent);
		
		List<Consent> consents = consentManagementDAOImpl.returnConsentRecordsFromLast24hours();
		
		assertEquals(1, consents.size());
	}

	@Test
	public void testSave()
	{
		Consent consent = returnConsent(null, "B1234", "N1234", LocalDate.parse("1975-02-01"), "TestFirst", "M", "TestLast", "TestMiddle", LocalDateTime.now());
		
		consentManagementDAOImpl.saveConsentDecision(consent);
		
	}
	
	@Test
	public void testRetrieveConsentDecisionType() throws Exception
	{
		assertEquals(2, consentManagementDAOImpl.retrieveConsentDecisionType(ConsentManagementConstants.CONSENT_DENIED).intValue());
		assertEquals(1, consentManagementDAOImpl.retrieveConsentDecisionType(ConsentManagementConstants.CONSENT_GRANTED).intValue());
		assertEquals(3, consentManagementDAOImpl.retrieveConsentDecisionType(ConsentManagementConstants.INMATE_NOT_INTERVIEWED).intValue());
		
	}
	
	@Test
	public void testUpdateConsentDecisionType() throws Exception
	{
		Consent consent = returnConsent(null, "B1234", "N1234", LocalDate.parse("1975-02-01"), "TestFirst", "M", "TestLast", "TestMiddle", LocalDateTime.now());
		
		Integer consentDecisionID = consentManagementDAOImpl.saveConsentDecision(consent);
		
		Integer consentDecisionTypeID = consentManagementDAOImpl.retrieveConsentDecisionType(ConsentManagementConstants.CONSENT_DENIED);
		
		consentManagementDAOImpl.updateConsentDecision(consentDecisionID, consentDecisionTypeID, "userId", "controlNumber",LocalDateTime.now());
		
		Consent updatedRecord = consentManagementDAOImpl.returnConsentRecordfromId(consentDecisionID);
		
		log.info(updatedRecord.toString());
		
		assertEquals(consentDecisionTypeID, updatedRecord.getConsentDecisionTypeID());
		assertEquals(consentDecisionID, updatedRecord.getConsentId());
		assertEquals("userId", updatedRecord.getConsenterUserID());
		assertEquals("controlNumber", updatedRecord.getConsentDocumentControlNumber());
		assertNotNull(updatedRecord.getConsentDecisionTimestamp());
		assertNotNull(updatedRecord.getRecordCreationTimestamp());
	}	

	private Consent returnConsent(Integer consentDecisionTypeID, String bookingNumber, String nameNumber, LocalDate personDob, String firstName, String gender, String lastName, String middleName, LocalDateTime recordCreationTimestamp) {
		Consent consent = new Consent();
		
		consent.setConsentDecisionTypeID(consentDecisionTypeID);
		consent.setBookingNumber(bookingNumber);
		consent.setNameNumber(nameNumber);
		consent.setPersonDOB(personDob);
		consent.setPersonFirstName(firstName);
		consent.setPersonGender(gender);
		consent.setPersonLastName(lastName);
		consent.setPersonMiddleName(middleName);
		consent.setRecordCreationTimestamp(recordCreationTimestamp);
		return consent;
	}
}
