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
package org.ojbc.bundles.utilities.auditing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.util.model.rapback.AgencyProfile;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

@UseAdviceWith	// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/test-beans.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml" })
public class TestAuditRestImpl {

	private Logger logger = Logger.getLogger(TestAuditRestImpl.class.getName());
	
    @Resource
    private ModelCamelContext context;

    @Resource
    private RestTemplate restTemplate;
    
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
    
    //This is used to update database to achieve desired state for test
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="dataSourceSubscriptions")
	private DataSource dataSource;

	@Before
	public void setUp() throws Exception {
		
    	context.start();
    	this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
    
	@Test
	public void testAuditRestImplSend() throws Exception
	{
		final String uri = "http://localhost:9898/auditServer/audit/printResults";
		
		PrintResults printResults = new PrintResults();
		
		printResults.setDescription("description");
		printResults.setMessageId("12345");
		printResults.setSystemName("system name");
		
		PrintResults printResultsResponse = restTemplate.postForObject(uri, printResults, PrintResults.class);
		
		logger.info(printResultsResponse.toString());
		
		assertEquals("description", printResultsResponse.getDescription());
		assertEquals("12345", printResultsResponse.getMessageId());
		assertEquals("system name", printResultsResponse.getSystemName());
		
		PrintResults printResultsFromDB = enhancedAuditDao.retrievePrintResultsfromMessageID("12345");
		
		assertEquals("description", printResultsFromDB.getDescription());
		assertEquals("12345", printResultsFromDB.getMessageId());
		assertEquals("system name", printResultsFromDB.getSystemName());
	}
	
	@Test
	public void testAuditRestUserLogin() throws Exception
	{
		final String uri = "http://localhost:9898/auditServer/audit/userLogin";
		
		UserInfo userInfo = new UserInfo();
		
		userInfo.setEmployerName("employer");
		userInfo.setEmployerSubunitName("sub");
		userInfo.setFederationId("fed");
		userInfo.setIdentityProviderId("idpID");
		userInfo.setUserEmailAddress("email");
		userInfo.setUserFirstName("first");
		userInfo.setUserLastName("last");
		
		UserInfo userInfoResults = restTemplate.postForObject(uri, userInfo, UserInfo.class);
		
		logger.info(userInfoResults.toString());
		
		assertEquals("employer", userInfoResults.getEmployerName());
		assertEquals("sub", userInfoResults.getEmployerSubunitName());
		assertEquals("fed", userInfoResults.getFederationId());
		assertEquals("idpID", userInfoResults.getIdentityProviderId());
		assertEquals("email", userInfoResults.getUserEmailAddress());
		assertEquals("first", userInfoResults.getUserFirstName());
		assertEquals("last", userInfoResults.getUserLastName());
		
	}
	
	@Test
	public void testAuditRestUserLogout() throws Exception
	{
		final String uri = "http://localhost:9898/auditServer/audit/userLogout";
		
		UserInfo userInfo = new UserInfo();
		
		userInfo.setEmployerName("employer");
		userInfo.setEmployerSubunitName("sub");
		userInfo.setFederationId("fed");
		userInfo.setIdentityProviderId("idpID");
		userInfo.setUserEmailAddress("email");
		userInfo.setUserFirstName("first");
		userInfo.setUserLastName("last");
		
		UserInfo userInfoResults = restTemplate.postForObject(uri, userInfo, UserInfo.class);
		
		logger.info(userInfoResults.toString());
		
		assertEquals("employer", userInfoResults.getEmployerName());
		assertEquals("sub", userInfoResults.getEmployerSubunitName());
		assertEquals("fed", userInfoResults.getFederationId());
		assertEquals("idpID", userInfoResults.getIdentityProviderId());
		assertEquals("email", userInfoResults.getUserEmailAddress());
		assertEquals("first", userInfoResults.getUserFirstName());
		assertEquals("last", userInfoResults.getUserLastName());
		
	}	
	
	@Test
	public void testSearchForFederalRapbackSubscriptionsByStateSubscriptionId() throws Exception
	{
		String uri = "http://localhost:9898/auditServer/audit/searchForFederalRapbackSubscriptionsByStateSubscriptionId";
		
		FederalRapbackSubscription federalRapbackSubscription = new FederalRapbackSubscription();
		
		federalRapbackSubscription.setTransactionControlReferenceIdentification("9999999");
		federalRapbackSubscription.setPathToRequestFile("/some/path/to/requestFile");
		federalRapbackSubscription.setRequestSentTimestamp(LocalDateTime.now());
		federalRapbackSubscription.setSid("123");
		federalRapbackSubscription.setStateSubscriptionId("456");
		federalRapbackSubscription.setSubscriptonCategoryCode("CS");
		federalRapbackSubscription.setTransactionStatusText("text");
		
		enhancedAuditDao.saveFederalRapbackSubscription(federalRapbackSubscription);

		ResponseEntity<List<FederalRapbackSubscription>> fedSubscriptionsResponse =
		        restTemplate.exchange(uri + "/456",
		                    HttpMethod.GET, null, new ParameterizedTypeReference<List<FederalRapbackSubscription>>() {
		            });
		List<FederalRapbackSubscription> fedSubscriptions = fedSubscriptionsResponse.getBody();
		
		assertNotNull(fedSubscriptions);
		assertEquals(1, fedSubscriptions.size());
		
		FederalRapbackSubscription federalRapbackSubscriptionFromDatabase =  fedSubscriptions.get(0);
		
		assertEquals("9999999", federalRapbackSubscriptionFromDatabase.getTransactionControlReferenceIdentification());
		assertEquals("/some/path/to/requestFile", federalRapbackSubscriptionFromDatabase.getPathToRequestFile());
		
		assertEquals("123", federalRapbackSubscriptionFromDatabase.getSid());
		assertEquals("456", federalRapbackSubscriptionFromDatabase.getStateSubscriptionId());
		assertEquals("CS", federalRapbackSubscriptionFromDatabase.getSubscriptonCategoryCode());
		assertEquals("text", federalRapbackSubscriptionFromDatabase.getTransactionStatusText());
		
	}
	
	@Test
	public void testRetrieveExpiringSubscriptions() throws Exception
	{
		String uri = "http://localhost:9898/auditServer/audit/retrieveExpiringSubscriptions";
		
    	DateTime now = new DateTime();
    	DateTime updatedEndDate= now.plusDays(1);
    	String updatedEndDateAsString = updatedEndDate.toString("yyyy-MM-dd");

    	//We will use these subscriptions for our tests, update their validation dates so they aren't filtered out
    	int rowsUpdated = this.jdbcTemplate.update("update SUBSCRIPTION set enddate='" + updatedEndDateAsString  + "', agency_case_number=12345 where ID ='62724'");
    	assertEquals(1, rowsUpdated);

    	rowsUpdated = this.jdbcTemplate.update("update SUBSCRIPTION set validationduedate='" + updatedEndDateAsString  + "', agency_case_number=56789 where ID ='62725'");
    	assertEquals(1, rowsUpdated);
		
		ExpiringSubscriptionRequest expiringSubscriptionRequest = new ExpiringSubscriptionRequest();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		List<String> oris = new ArrayList<String>();
		oris.add("1234567890");
		expiringSubscriptionRequest.setOris(oris);
		
		expiringSubscriptionRequest.setDaysUntilExpiry(2);
		expiringSubscriptionRequest.setSystemName("{http://demostate.gov/SystemNames/1.0}SystemC");
		
		HttpEntity<ExpiringSubscriptionRequest> entity = new HttpEntity<ExpiringSubscriptionRequest>(expiringSubscriptionRequest, headers);
		
		ResponseEntity<List<Subscription>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<Subscription>>() {});
		
		List<Subscription> subscriptions = response.getBody();
		
		assertEquals(2, subscriptions.size());
		assertEquals("2014-10-15",subscriptions.get(0).getStartDate().toString("yyyy-MM-dd"));
		assertEquals(updatedEndDateAsString,subscriptions.get(0).getEndDate().toString("yyyy-MM-dd"));
		assertEquals(now.toString("yyyy-MM-dd"),subscriptions.get(0).getValidationDueDate().toString("yyyy-MM-dd"));
		assertEquals("2014-10-15",subscriptions.get(0).getCreationDate().toString("yyyy-MM-dd"));
		
		assertEquals("12345",subscriptions.get(0).getAgencyCaseNumber());
		assertEquals("Test W Jane",subscriptions.get(0).getPersonFullName());
		assertEquals("1234567890",subscriptions.get(0).getOri());
		assertEquals("Demo Agency",subscriptions.get(0).getAgencyName());
		assertEquals("bill",subscriptions.get(0).getSubscriptionOwnerFirstName());
		assertEquals("padmanabhan",subscriptions.get(0).getSubscriptionOwnerLastName());
		assertEquals("12345",subscriptions.get(0).getAgencyCaseNumber());

		assertEquals("56789",subscriptions.get(1).getAgencyCaseNumber());

	}

	@Test
	public void testRetrieveExpiredSubscriptionsPojo() throws Exception
	{
		String uri = "http://localhost:9898/auditServer/audit/retrieveExpiredSubscriptions";
		
    	DateTime now = new DateTime();
    	DateTime updatedEndDate= now.minusDays(1);
    	String updatedEndDateAsString = updatedEndDate.toString("yyyy-MM-dd");

    	//We will use these subscriptions for our tests, update their validation dates so they aren't filtered out
    	int rowsUpdated = this.jdbcTemplate.update("update SUBSCRIPTION set enddate='" + updatedEndDateAsString  + "', agency_case_number='12345' where ID ='62724'");
    	assertEquals(1, rowsUpdated);
    	
    	rowsUpdated = this.jdbcTemplate.update("update SUBSCRIPTION set validationduedate='" + updatedEndDateAsString  + "', agency_case_number=56789 where ID ='62725'");
    	assertEquals(1, rowsUpdated);    	
    			
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ExpiringSubscriptionRequest expiringSubscriptionRequest = new ExpiringSubscriptionRequest();
		
		List<String> oris = new ArrayList<String>();
		oris.add("1234567890");
		
		expiringSubscriptionRequest.setDaysUntilExpiry(2);

		expiringSubscriptionRequest.setOris(oris);
		
		expiringSubscriptionRequest.setSystemName("{http://demostate.gov/SystemNames/1.0}SystemC");
		
		HttpEntity<ExpiringSubscriptionRequest> entity = new HttpEntity<ExpiringSubscriptionRequest>(expiringSubscriptionRequest, headers);
		
		ResponseEntity<List<Subscription>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<Subscription>>() {});
		
		List<Subscription> subscriptions = response.getBody();
		
		assertEquals(2, subscriptions.size());
		assertEquals("2014-10-15",subscriptions.get(0).getStartDate().toString("yyyy-MM-dd"));
		assertEquals(updatedEndDateAsString,subscriptions.get(0).getEndDate().toString("yyyy-MM-dd"));
		assertEquals(now.toString("yyyy-MM-dd"),subscriptions.get(0).getValidationDueDate().toString("yyyy-MM-dd"));
		assertEquals("2014-10-15",subscriptions.get(0).getCreationDate().toString("yyyy-MM-dd"));
		
		assertEquals("12345",subscriptions.get(0).getAgencyCaseNumber());
		assertEquals("Test W Jane",subscriptions.get(0).getPersonFullName());
		assertEquals("1234567890",subscriptions.get(0).getOri());
		assertEquals("Demo Agency",subscriptions.get(0).getAgencyName());
		assertEquals("bill",subscriptions.get(0).getSubscriptionOwnerFirstName());
		assertEquals("padmanabhan",subscriptions.get(0).getSubscriptionOwnerLastName());
		assertEquals("12345",subscriptions.get(0).getAgencyCaseNumber());

		assertEquals("56789",subscriptions.get(1).getAgencyCaseNumber());
		
	}
	
	
	@Test
	public void testReturnAllAgencies() throws Exception
	{
		String uri = "http://localhost:9898/auditServer/audit/retrieveAllAgencies";
		
		ResponseEntity<List<AgencyProfile>> agencyProfileResponse =
		        restTemplate.exchange(uri,
		                    HttpMethod.GET, null, new ParameterizedTypeReference<List<AgencyProfile>>() {
		            });
		
		List<AgencyProfile> agencyProfiles = agencyProfileResponse.getBody();
		
		assertNotNull(agencyProfiles);
		assertEquals(3, agencyProfiles.size());
		
		AgencyProfile agency1 = agencyProfiles.get(0);
		
		assertEquals("Demo Agency",agency1.getAgencyName());
		assertEquals("1234567890",agency1.getAgencyOri());
		assertEquals(false,agency1.getCivilAgencyIndicator());
		assertEquals(true,agency1.getFbiSubscriptionQualification());

	}
	
}
