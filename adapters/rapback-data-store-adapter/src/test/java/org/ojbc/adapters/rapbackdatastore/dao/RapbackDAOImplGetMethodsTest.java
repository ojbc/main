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
package org.ojbc.adapters.rapbackdatastore.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.rapbackdatastore.dao.model.AgencyProfile;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.test.util.SAMLTokenTestUtils;
import org.ojbc.util.model.rapback.IdentificationResultSearchRequest;
import org.ojbc.util.model.rapback.IdentificationTransactionState;
import org.ojbc.util.model.saml.SamlAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml"
		})
@DirtiesContext
public class RapbackDAOImplGetMethodsTest {

    @Value("${rapbackDatastoreAdapter.civilIdlePeriod:60}")
    private Integer civilIdlePeriod;
    
	private final Log log = LogFactory.getLog(this.getClass());
    
	@Autowired
	RapbackDAOImpl rapbackDAO;
	
	@Resource
	FbiRapbackDao fbiSubscriptionDao;
	
    @Resource  
    private DataSource dataSource;  

	@Before
	public void setUp() throws Exception {
		assertNotNull(rapbackDAO);
		assertNotNull(fbiSubscriptionDao);
	}
	
	@Test
	public void testRapbackDatastore() throws Exception {
		
		Connection conn = dataSource.getConnection();
		ResultSet rs = conn.createStatement().executeQuery("select * from subscription");
		assertTrue(rs.next());
		assertEquals(62720,rs.getInt("id"));
		rs = conn.createStatement().executeQuery("select * from IDENTIFICATION_SUBJECT");
	}
	
	@Test
	public void testGetCivilIdentificationTransactionsWithReasonCode() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demoUser");
		customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Honolulu PD Records and ID Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "Sworn Supervisors");
		  
		SAMLTokenPrincipal samlAssertionSuperUser = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
      
		IdentificationResultSearchRequest searchRequest = new IdentificationResultSearchRequest();
		List<String> status = new ArrayList<String>();
		status.add(IdentificationTransactionState.Available_for_Subscription.toString());
		status.add(IdentificationTransactionState.Subscribed.toString());
		searchRequest.setIdentificationTransactionStatus(status);
		
		List<String> reasonCodes = new ArrayList<String>();
		reasonCodes.add("F");
		reasonCodes.add("I");
		searchRequest.setCivilIdentificationReasonCodes(reasonCodes);

		List<IdentificationTransaction> transactionsForSuperUserWithReasonCode = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(2, transactionsForSuperUserWithReasonCode.size());
	}

	@Test
	public void testGetCivilIdentificationTransactionsWithoutReasonCode() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demoUser");
		customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Honolulu PD Records and ID Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "No Position");
		  
		SAMLTokenPrincipal samlAssertionDemoUser = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
      
		IdentificationResultSearchRequest searchRequest = new IdentificationResultSearchRequest();
		List<String> status = new ArrayList<String>();
		status.add(IdentificationTransactionState.Available_for_Subscription.toString());
		status.add(IdentificationTransactionState.Subscribed.toString());
		searchRequest.setIdentificationTransactionStatus(status);
		
		List<String> reasonCodes = new ArrayList<String>();
		searchRequest.setCivilIdentificationReasonCodes(reasonCodes);

		List<IdentificationTransaction> transactionsForSuperUserWithReasonCode = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionDemoUser, searchRequest);
		assertEquals(0, transactionsForSuperUserWithReasonCode.size());
	}

	@Test
	public void testGetCivilIdentificationTransactions() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:normaluser");
		customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Honolulu PD Records and ID Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "Sworn Supervisors");
		  
		SAMLTokenPrincipal samlAssertion = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
      
		IdentificationResultSearchRequest searchRequest = new IdentificationResultSearchRequest();
		List<IdentificationTransaction> transactions = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertion, searchRequest);
		assertEquals(4, transactions.size());
		
		customAttributes.put(SamlAttribute.EmployeePositionName, "Firearms Unit (both Civilian and Sworn)");
		SAMLTokenPrincipal samlAssertionWithOnlyOneViewableCategory = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
		
		List<IdentificationTransaction> transactions2 = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionWithOnlyOneViewableCategory, searchRequest);
		assertEquals(2, transactions2.size());
		
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser");
		SAMLTokenPrincipal samlAssertionSuperUser = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
		
		
		List<IdentificationTransaction> transactionsForSuperUser = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(4, transactionsForSuperUser.size());
		
		List<String> status = new ArrayList<String>();
		status.add(IdentificationTransactionState.Available_for_Subscription.toString());
		status.add(IdentificationTransactionState.Subscribed.toString());
		searchRequest.setIdentificationTransactionStatus(status);
		
		List<IdentificationTransaction> transactionsForSuperUserWithStatusCriteria = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(4, transactionsForSuperUserWithStatusCriteria.size());
		
		
		List<String> reasonCodes = new ArrayList<String>();
		reasonCodes.add("J");
		searchRequest.setCivilIdentificationReasonCodes(reasonCodes);
		List<IdentificationTransaction> transactionsForSuperUserWithReasonCode = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(2, transactionsForSuperUserWithReasonCode.size());
		
		reasonCodes.clear();
		status.clear();
		status.add(IdentificationTransactionState.Available_for_Subscription.toString());
		List<IdentificationTransaction> transactionsForSuperUserWithOnlyAvailableForSubscription = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(1, transactionsForSuperUserWithOnlyAvailableForSubscription.size());
		
		status.clear();
		status.add(IdentificationTransactionState.Subscribed.toString());
		status.add(IdentificationTransactionState.Archived.toString());
		List<IdentificationTransaction> transactionsForSuperUserWithSubscribedOrArchived = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(3, transactionsForSuperUserWithSubscribedOrArchived.size());
		
		searchRequest.setFirstName("Lisa");
		List<IdentificationTransaction> transactionsForSuperUserWithSubscribedOrArchivedLisa = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(1, transactionsForSuperUserWithSubscribedOrArchivedLisa.size());
		
		searchRequest.setFirstName(null);
		status.clear();
		status.add(IdentificationTransactionState.Available_for_Subscription.toString());
		status.add(IdentificationTransactionState.Subscribed.toString());
		searchRequest.setIdentificationTransactionStatus(null);
		searchRequest.setReportedDateStartLocalDate(LocalDate.parse("2013-10-20"));
		searchRequest.setReportedDateEndLocalDate(LocalDate.parse("2016-06-10"));
		searchRequest.setIdentificationResultCategory("Civil");

		List<IdentificationTransaction> transactionsForSuperUserWithDateRangeCriteria = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(2, transactionsForSuperUserWithDateRangeCriteria.size());

		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:civilruser");
		customAttributes.put(SamlAttribute.EmployerORI, "68796860");
		SAMLTokenPrincipal samlAssertionCivilUser = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);

		searchRequest.setReportedDateStartDate(null);
		List<IdentificationTransaction> transactionsForCivilUser = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionCivilUser, searchRequest);
		assertEquals(1, transactionsForCivilUser.size());
		
	}
	
	@Test
	public void testGetCriminalIdentificationTransactions() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:normaluser");
		customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Honolulu PD Records and ID Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "Sworn Supervisors");
		  
		SAMLTokenPrincipal samlAssertion = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
		IdentificationResultSearchRequest searchRequest = new IdentificationResultSearchRequest();
		
		List<IdentificationTransaction> transactions = 
				rapbackDAO.getCriminalIdentificationTransactions(samlAssertion, searchRequest);
		assertEquals(1, transactions.size());
		
		customAttributes.put(SamlAttribute.EmployeePositionName, "Firearms Unit (both Civilian and Sworn)");
		SAMLTokenPrincipal samlAssertionWithOnlyNoViewableCategory = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
		
		List<IdentificationTransaction> transactions2 = 
				rapbackDAO.getCriminalIdentificationTransactions(samlAssertionWithOnlyNoViewableCategory, searchRequest);
		assertEquals(0, transactions2.size());
		
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser");
		SAMLTokenPrincipal samlAssertionSuperUser = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
		
		List<IdentificationTransaction> transactionsForSuperUser = 
				rapbackDAO.getCriminalIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(1, transactionsForSuperUser.size());
	}
	
	@Test
	public void testGetCivilInitialResultsByTransactionNumber() throws Exception {
		List<CivilInitialResults> civilInitialResults= 
				rapbackDAO.getIdentificationCivilInitialResults("000001820140729014008339990");
		log.info("Civil Initial Results count: " + civilInitialResults.size());
		assertEquals(2, civilInitialResults.size());
		log.info("Search result doc content: " + new String(civilInitialResults.get(0).getSearchResultFile()));
		assertEquals(2110, civilInitialResults.get(0).getSearchResultFile().length);
	}
	
	@Test
	public void testGetCivilInitialResults() throws Exception {
		List<CivilInitialResults> civilInitialResults= 
				rapbackDAO.getIdentificationCivilInitialResults("000001820140729014008339995");
		log.info("Civil Initial Results count: " + civilInitialResults.size());
		assertEquals(2, civilInitialResults.size());
		log.info("Search result doc content: " + new String(civilInitialResults.get(0).getSearchResultFile()));
		assertEquals(1832, civilInitialResults.get(0).getSearchResultFile().length);
	}
	
	@Test
	public void testGetCriminalInitialResults() throws Exception {
		List<CriminalInitialResults> criminalInitialResults= 
				rapbackDAO.getIdentificationCriminalInitialResults("000001820140729014008339994");
		log.info("Criminal Initial Results count: " + criminalInitialResults.size());
		assertEquals(2, criminalInitialResults.size());
		log.info("Search result doc content: " + new String(criminalInitialResults.get(0).getSearchResultFile()));
		assertEquals(1832, criminalInitialResults.get(0).getSearchResultFile().length);
	}
	
	@Test
	public void testGetSubsequentResults() throws Exception {
		
		List<SubsequentResults> subsequentResults = rapbackDAO.getSubsequentResults("000001820140729014008339995");
		assertEquals(2, subsequentResults.size());
		SubsequentResults result1 = subsequentResults.get(0);
		assertEquals("9222201", result1.getUcn());
		assertEquals(ResultSender.FBI, result1.getResultsSender());
		
		SubsequentResults result2 = subsequentResults.get(1);
		assertEquals(Long.valueOf(2), result2.getId());
		assertEquals("9222201", result2.getUcn());
		assertEquals(ResultSender.State, result2.getResultsSender());
		assertEquals(25, result2.getRapSheet().length);
		log.info("result2 result:" + new String(result2.getRapSheet()));
		log.info("result2 result size:" + result2.getRapSheet().length);
		
		List<SubsequentResults> emptySubsequentResults = rapbackDAO.getSubsequentResults("000001820140729014008339999");
		assertEquals(0, emptySubsequentResults.size());
	}

	@Test
	public void testGetAgencyProfile() throws Exception {
		AgencyProfile agencyProfile = rapbackDAO.getAgencyProfile("1234567890");
		log.info(agencyProfile.toString());
		assertEquals(Integer.valueOf(1), agencyProfile.getId());
		assertEquals("1234567890", agencyProfile.getAgencyOri());
		assertEquals("Demo Agency", agencyProfile.getAgencyName());
		assertEquals(Boolean.TRUE, agencyProfile.getFbiSubscriptionQualified());
		assertEquals("demo.agency@localhost", agencyProfile.getEmails().get(0));
		assertEquals("demo.agency2@localhost", agencyProfile.getEmails().get(1));
		
		AgencyProfile agencyProfileNull = rapbackDAO.getAgencyProfile("123456789");
		assertNull(agencyProfileNull);
	}
}
