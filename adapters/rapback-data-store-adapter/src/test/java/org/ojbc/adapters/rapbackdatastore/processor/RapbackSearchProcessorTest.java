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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.adapters.rapbackdatastore.application.RapbackDatastoreAdapterApplication;
import org.ojbc.test.util.SAMLTokenTestUtils;
import org.ojbc.test.util.XmlTestUtils;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.model.rapback.IdentificationResultSearchRequest;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;

import jakarta.annotation.Resource;

@CamelSpringBootTest
@SpringBootTest(classes=RapbackDatastoreAdapterApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
        "classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
		})
public class RapbackSearchProcessorTest {
	private static final Log log = LogFactory.getLog( RapbackSearchProcessorTest.class );

	@Resource 
	RapbackSearchProcessor rapbackSearchProcessor; 
	
    //This is used to update database to achieve desired state for test
    private JdbcTemplate jdbcTemplate;
	
    @Resource  
    private DataSource dataSource;  
    
	@BeforeEach
	public void setUp() throws Exception {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		assertNotNull(rapbackSearchProcessor);
		
    	//We will use these subscriptions for our tests, update their validation dates so they aren't filtered out
    	int rowsUpdated = this.jdbcTemplate.update("update subscription set validationDueDate = '2011-10-18' where id=62720 or id=62721 or id=62722");
    	assertEquals(3, rowsUpdated);
    	
    	rowsUpdated = this.jdbcTemplate.update("update subscription set validationDueDate = '2015-09-18' where id=62725");
    	assertEquals(1, rowsUpdated);

    	rowsUpdated = this.jdbcTemplate.update("update subscription set validationDueDate = '2015-10-15' where id=62726 or id=62727");
    	assertEquals(2, rowsUpdated);
	}

	@Test
	public void testSingleAgencySuperUserSearchResult() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser");

        org.apache.cxf.message.Message message = 
        		SAMLTokenTestUtils.createSamlAssertionMessageWithAttributes(customAttributes);
        
        Document civilIdentificationSearchRequest = 
        		XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/"
        				+ "rapbackSearch/OrganizationIdentificationResultsSearchRequest-Civil.xml"));
        
        Document searchResponeDoc = rapbackSearchProcessor.returnRapbackSearchResponse(message, civilIdentificationSearchRequest);
        
        log.info("Civil identification search Response: \n" + OJBUtils.getStringFromDocument(searchResponeDoc));
		XmlTestUtils.compareDocuments(
        		"src/test/resources/xmlInstances/rapbackSearch/CivilIdentficationSearchResponseAgencySuperUser.xml",
        		searchResponeDoc);
	}

	@Test
	public void testMultiAgencySuperUserSearchResult() throws Exception {
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.EmployerORI, "68796860");
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:hpotter");
		
		org.apache.cxf.message.Message message = 
				SAMLTokenTestUtils.createSamlAssertionMessageWithAttributes(customAttributes);
		
		Document civilIdentificationSearchRequest = 
				XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/"
						+ "rapbackSearch/OrganizationIdentificationResultsSearchRequest-Civil.xml"));
		
		Document searchResponeDoc = rapbackSearchProcessor.returnRapbackSearchResponse(message, civilIdentificationSearchRequest);
		
		log.info("Civil identification search Response: \n" + OJBUtils.getStringFromDocument(searchResponeDoc));
		XmlTestUtils.compareDocuments(
				"src/test/resources/xmlInstances/rapbackSearch/CivilIdentficationSearchResponseSuperUser.xml",
				searchResponeDoc);
	}
	
	@Test
	public void testSuperUserCivilSearchResult() throws Exception {
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.EmployerORI, "HCJDC");
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:superuser");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Honolulu PD Records and ID Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "Sworn Supervisors");
		
		org.apache.cxf.message.Message message = 
				SAMLTokenTestUtils.createSamlAssertionMessageWithAttributes(customAttributes);
		
		Document civilIdentificationSearchRequest = 
				XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/"
						+ "rapbackSearch/OrganizationIdentificationResultsSearchRequest-Civil.xml"));
		
		Document searchResponeDoc = rapbackSearchProcessor.returnRapbackSearchResponse(message, civilIdentificationSearchRequest);
		
		log.info("Civil identification search Response: \n" + OJBUtils.getStringFromDocument(searchResponeDoc));
		XmlTestUtils.compareDocuments(
				"src/test/resources/xmlInstances/rapbackSearch/CivilIdentficationSearchResponseSuperUser.xml",
				searchResponeDoc);
	}
	
	@Test
	public void testSuperUserCriminalSearchResult() throws Exception {
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.EmployerORI, "HCJDC");
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:superuser");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Honolulu PD Records and ID Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "Sworn Supervisors");
		
		org.apache.cxf.message.Message message = 
				SAMLTokenTestUtils.createSamlAssertionMessageWithAttributes(customAttributes);
		
		Document criminalIdentificationSearchRequest = 
				XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/"
						+ "rapbackSearch/OrganizationIdentificationResultsSearchRequest-Criminal.xml"));
		
		Document criminalSearchResponeDoc = rapbackSearchProcessor.returnRapbackSearchResponse(message, criminalIdentificationSearchRequest);
		
		log.info("Criminal identification search Response: \n" + OJBUtils.getStringFromDocument(criminalSearchResponeDoc));
		XmlTestUtils.compareDocuments(
				"src/test/resources/xmlInstances/rapbackSearch/CriminalIdentficationSearchResponseSuperUser.xml", 
				criminalSearchResponeDoc);
	}
	
	@Test
	public void testTitledUserCriminalSearchResult() throws Exception {
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:titleduser");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Central Receiving Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "All Sworn Personnel");
		
		org.apache.cxf.message.Message message = 
				SAMLTokenTestUtils.createSamlAssertionMessageWithAttributes(customAttributes);
		
		Document criminalIdentificationSearchRequest = 
				XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/"
						+ "rapbackSearch/OrganizationIdentificationResultsSearchRequest-Criminal.xml"));
		
		Document criminalSearchResponeDoc = rapbackSearchProcessor.returnRapbackSearchResponse(message, criminalIdentificationSearchRequest);
		
		log.info("Criminal identification search Response: \n" + OJBUtils.getStringFromDocument(criminalSearchResponeDoc));
		XmlTestUtils.compareDocuments(
				"src/test/resources/xmlInstances/rapbackSearch/CriminalIdentficationSearchResponseForTitledUser.xml",
				criminalSearchResponeDoc);
	}
	
	@Test
	public void testTitledUserCivilSearchResult() throws Exception {
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:titleduser");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Central Receiving Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "All Sworn Personnel");
		
		org.apache.cxf.message.Message message = 
				SAMLTokenTestUtils.createSamlAssertionMessageWithAttributes(customAttributes);
		
		Document civilIdentificationSearchRequest = 
				XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/"
						+ "rapbackSearch/OrganizationIdentificationResultsSearchRequest-Civil.xml"));
		
		Document searchResponeDoc = rapbackSearchProcessor.returnRapbackSearchResponse(message, civilIdentificationSearchRequest);
		
		log.info("Civil identification search Response: \n" + OJBUtils.getStringFromDocument(searchResponeDoc));
		IdentificationReportingResponseProcessorTest.assertAsExpected(
				OJBUtils.getStringFromDocument(searchResponeDoc), 
				"src/test/resources/xmlInstances/rapbackSearch/CivilIdentficationEmptySearchResponseForTitledUser.xml");
		
		customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:titleduser");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Honolulu PD Records and ID Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "Fingerprint Technicians");
		
		message = SAMLTokenTestUtils.createSamlAssertionMessageWithAttributes(customAttributes);
		
		searchResponeDoc = rapbackSearchProcessor.returnRapbackSearchResponse(message, civilIdentificationSearchRequest);
		
		log.info("Civil identification search Response: \n" + OJBUtils.getStringFromDocument(searchResponeDoc));
		XmlTestUtils.compareDocuments(
				"src/test/resources/xmlInstances/rapbackSearch/CivilIdentficationSearchResponseForTitledUser.xml", 
				searchResponeDoc);
	}
	
	@Test
	public void testAnyTitledUserCivilSearchResult() throws Exception {
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:titleduser");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Test Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "Any");
		
		org.apache.cxf.message.Message message = 
				SAMLTokenTestUtils.createSamlAssertionMessageWithAttributes(customAttributes);
		
		Document civilIdentificationSearchRequest = 
				XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/"
						+ "rapbackSearch/OrganizationIdentificationResultsSearchRequest-Civil.xml"));
		
		Document searchResponeDoc = rapbackSearchProcessor.returnRapbackSearchResponse(message, civilIdentificationSearchRequest);
		
		log.info("Civil identification search Response: \n" + OJBUtils.getStringFromDocument(searchResponeDoc));
		XmlTestUtils.compareDocuments(
				"src/test/resources/xmlInstances/rapbackSearch/CivilIdentficationSearchResponseForAnyTitleUser.xml",
				searchResponeDoc);
	}
	
	@Test
	public void testCivilUserSearchResult() throws Exception {
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.EmployerORI, "68796860");
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:civiluser");
		
		org.apache.cxf.message.Message message = 
				SAMLTokenTestUtils.createSamlAssertionMessageWithAttributes(customAttributes);
		
		Document civilIdentificationSearchRequest = 
				XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/"
						+ "rapbackSearch/OrganizationIdentificationResultsSearchRequest-Civil.xml"));
		
		Document searchResponeDoc = rapbackSearchProcessor.returnRapbackSearchResponse(message, civilIdentificationSearchRequest);
		
		log.info("Civil identification search Response: \n" + OJBUtils.getStringFromDocument(searchResponeDoc));
		
		XmlTestUtils.compareDocuments("src/test/resources/xmlInstances/rapbackSearch/CivilIdentficationSearchResponseCivilUser.xml", 
				searchResponeDoc);
	}
	
	@Test 
	public void testExtractSearchRequestFromXml() throws Exception {
        Document criminalIdentificationSearchRequest = 
        		XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/"
        				+ "rapbackSearch/IdentificationResultsSearchRequestWithCriteria-Criminal.xml"));
        IdentificationResultSearchRequest searchRequest = rapbackSearchProcessor.getSearchRequestFromXml(criminalIdentificationSearchRequest);
        
        log.info(searchRequest);
        assertNull(searchRequest.getIdentificationResultCategory());
        assertArrayEquals(new String[]{"Available for Subscription", "Subscribed"}, searchRequest.getIdentificationTransactionStatus().toArray() );
        assertEquals(LocalDate.parse("2011-01-01"), searchRequest.getReportedDateStartLocalDate());
        assertEquals(LocalDate.parse("2016-01-01"), searchRequest.getReportedDateEndLocalDate());
        assertEquals("Walter", searchRequest.getFirstName());
        assertEquals("White", searchRequest.getLastName());
        assertEquals("12345678", searchRequest.getOtn());
        assertTrue(searchRequest.getCivilIdentificationReasonCodes().size() == 0);
        assertArrayEquals(new String[]{"SOR", "CAR"}, searchRequest.getCriminalIdentificationReasonCodes().toArray() );
        
        Document civilIdentificationSearchRequest = 
        		XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/"
        				+ "rapbackSearch/IdentificationResultsSearchRequestWithCriteria-Civil.xml"));
        IdentificationResultSearchRequest civilSearchRequest = rapbackSearchProcessor.getSearchRequestFromXml(civilIdentificationSearchRequest);
        log.info(civilSearchRequest);
        assertArrayEquals(new String[]{"Available for Subscription", "Subscribed"}, civilSearchRequest.getIdentificationTransactionStatus().toArray() );
        assertEquals(LocalDate.parse("2011-01-01"), civilSearchRequest.getNotificationDateStartLocalDate());
        assertEquals(LocalDate.parse("2016-01-01"), civilSearchRequest.getNotificationDateEndLocalDate());
        
    }
}
