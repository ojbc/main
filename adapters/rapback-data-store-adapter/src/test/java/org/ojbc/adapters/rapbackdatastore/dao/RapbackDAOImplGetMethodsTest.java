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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.adapters.rapbackdatastore.application.RapbackDatastoreAdapterApplication;
import org.ojbc.adapters.rapbackdatastore.dao.model.AgencyProfile;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.test.util.SAMLTokenTestUtils;
import org.ojbc.util.helper.ZipUtils;
import org.ojbc.util.model.rapback.IdentificationResultSearchRequest;
import org.ojbc.util.model.rapback.IdentificationTransactionState;
import org.ojbc.util.model.saml.SamlAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

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
    
    @Resource
    private JdbcTemplate jdbcTemplate;

	@BeforeEach
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
		status.add(IdentificationTransactionState.Subscribed_State.toString());
		status.add(IdentificationTransactionState.Subscribed_State_FBI.toString());
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
		status.add(IdentificationTransactionState.Subscribed_State.toString());
		status.add(IdentificationTransactionState.Subscribed_State_FBI.toString());
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
		status.add(IdentificationTransactionState.Subscribed_State.toString());
		status.add(IdentificationTransactionState.Subscribed_State_FBI.toString());
		searchRequest.setIdentificationTransactionStatus(status);
		
		List<IdentificationTransaction> transactionsForSuperUserWithStatusCriteria = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(3, transactionsForSuperUserWithStatusCriteria.size());
		
		
		List<String> reasonCodes = new ArrayList<String>();
		reasonCodes.add("J");
		searchRequest.setCivilIdentificationReasonCodes(reasonCodes);
		List<IdentificationTransaction> transactionsForSuperUserWithReasonCode = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(1, transactionsForSuperUserWithReasonCode.size());
		
		reasonCodes.clear();
		status.clear();
		status.add(IdentificationTransactionState.Available_for_Subscription.toString());
		List<IdentificationTransaction> transactionsForSuperUserWithOnlyAvailableForSubscription = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(0, transactionsForSuperUserWithOnlyAvailableForSubscription.size());
		
		status.clear();
		status.add(IdentificationTransactionState.Subscribed_State.toString());
		status.add(IdentificationTransactionState.Subscribed_State_FBI.toString());
		status.add(IdentificationTransactionState.Archived.toString());
		List<IdentificationTransaction> transactionsForSuperUserWithSubscribedOrArchived = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(4, transactionsForSuperUserWithSubscribedOrArchived.size());
		
		searchRequest.setFirstName("Lisa");
		List<IdentificationTransaction> transactionsForSuperUserWithSubscribedOrArchivedLisa = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser, searchRequest);
		assertEquals(1, transactionsForSuperUserWithSubscribedOrArchivedLisa.size());
		
		searchRequest.setFirstName(null);
		status.clear();
		status.add(IdentificationTransactionState.Available_for_Subscription.toString());
		status.add(IdentificationTransactionState.Subscribed_State.toString());
		status.add(IdentificationTransactionState.Subscribed_State_FBI.toString());
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
	
	final String CIVIL_FINGER_PRINTS_UPDATE = "update rapback_datastore.CIVIL_FINGER_PRINTS "
            + "set FINGER_PRINTS_FILE = ? where TRANSACTION_NUMBER = ? and FINGER_PRINTS_TYPE_ID = ?";
    
    final String CIVIL_INITIAL_RESULTS_UPDATE = "update rapback_datastore.CIVIL_INITIAL_RESULTS "
            + "set SEARCH_RESULT_FILE = ? where TRANSACTION_NUMBER = ? and RESULTS_SENDER_ID = ?";
    
    final String CIVIL_RAPSHEET_UPDATE = "update rapback_datastore.CIVIL_INITIAL_RAP_SHEET "
            + "set RAP_SHEET = ? where CIVIL_INITIAL_RESULT_ID = ?";

    final String NSOR_DEMOGRAPHICS_UPDATE = "update rapback_datastore.NSOR_DEMOGRAPHICS "
            + "set DEMOGRAPHICS_FILE = ? where TRANSACTION_NUMBER = ? and NSOR_DEMOGRAPHICS_ID = ?";
    
    final String NSOR_SEARCH_RESULTS_UPDATE = "update rapback_datastore.NSOR_SEARCH_RESULT "
            + "set SEARCH_RESULT_FILE = ? where TRANSACTION_NUMBER = ? and NSOR_SEARCH_RESULT_ID = ?";
	
	@Test
	public void testGetCivilInitialResultsByTransactionNumber() throws Exception {
	    byte[] fbiCivilFingerprints = ZipUtils.zip("FBICivilFingerprints".getBytes());
        jdbcTemplate.update(CIVIL_FINGER_PRINTS_UPDATE, fbiCivilFingerprints, "000001820140729014008339990", 1);
        
        byte[] stateCivilFingerprints = ZipUtils.zip("StateCivilFingerprints".getBytes());
        jdbcTemplate.update(CIVIL_FINGER_PRINTS_UPDATE, stateCivilFingerprints, "000001820140729014008339990", 2);
        
        byte[] fbiCivilResults = ZipUtils.zip("FBICivilInitialResults".getBytes());
        jdbcTemplate.update(CIVIL_INITIAL_RESULTS_UPDATE, fbiCivilResults, "000001820140729014008339990", 1);
        
        byte[] stateCivilResults = ZipUtils.zip("StateCivilInitialResults".getBytes());
        jdbcTemplate.update(CIVIL_INITIAL_RESULTS_UPDATE, stateCivilResults, "000001820140729014008339990", 2);
        
        byte[] civilRapSheet = ZipUtils.zip("CivilInitialResultsRapsheet1".getBytes());
        jdbcTemplate.update(CIVIL_RAPSHEET_UPDATE, civilRapSheet, 1);
        
        byte[] civilRapSheet2 = ZipUtils.zip("CivilInitialResultsRapsheet2".getBytes());
        jdbcTemplate.update(CIVIL_RAPSHEET_UPDATE, civilRapSheet2, 2);
        
        byte[] nsorDemo = ZipUtils.zip("NsorDemographics".getBytes());
        jdbcTemplate.update(NSOR_DEMOGRAPHICS_UPDATE, nsorDemo, "000001820140729014008339990", 1);
        
        byte[] nsorDemo1 = ZipUtils.zip("NsorDemographics1".getBytes());
        jdbcTemplate.update(NSOR_DEMOGRAPHICS_UPDATE, nsorDemo1, "000001820140729014008339990", 2);
        
        byte[] nsorSearch = ZipUtils.zip("NsorSearchResults".getBytes());
        jdbcTemplate.update(NSOR_SEARCH_RESULTS_UPDATE, nsorSearch, "000001820140729014008339990", 1);
        
        byte[] nsorSearch1 = ZipUtils.zip("NsorSearchResults1".getBytes());
        jdbcTemplate.update(NSOR_SEARCH_RESULTS_UPDATE, nsorSearch1, "000001820140729014008339990", 2);
        
		List<CivilInitialResults> civilInitialResults= 
				rapbackDAO.getIdentificationCivilInitialResults("000001820140729014008339990");
		log.info("Civil Initial Results count: " + civilInitialResults.size());
		assertEquals(2, civilInitialResults.size());
		log.info("Search result doc content: " + new String(civilInitialResults.get(0).getSearchResultFile()));
		assertEquals(22, civilInitialResults.get(0).getSearchResultFile().length);
		
		assertNotNull(civilInitialResults.get(0).getIdentificationTransaction());
		assertNotNull(civilInitialResults.get(1).getIdentificationTransaction());
		assertTrue(civilInitialResults.get(0).getIdentificationTransaction().getSubject().equals(
				civilInitialResults.get(1).getIdentificationTransaction().getSubject()));
		IdentificationTransaction identificationTransaction = civilInitialResults.get(0).getIdentificationTransaction();
		log.info("Idnetification: " + identificationTransaction);
		assertThat(identificationTransaction.getOwnerAgencyName(), equalTo("Test Agency"));
		assertNotNull(identificationTransaction.getSubscription());
		assertThat(identificationTransaction.getHavingSubsequentResults(), equalTo(false));
		assertThat(identificationTransaction.getHavingNsorFiveYearCheck(), equalTo(false));
		Subject subject = civilInitialResults.get(0).getIdentificationTransaction().getSubject(); 
		log.info("Subject: " + subject);
		assertThat(subject.getFirstName(), equalTo("Test"));
		assertThat(subject.getLastName(), equalTo("Jane"));
		assertThat(subject.getMiddleInitial(), equalTo("W"));
		assertThat(subject.getCriminalSid(), equalTo("C1234567"));
		assertThat(subject.getCivilSid(), equalTo("A123457"));
		assertThat(subject.getUcn(), equalTo("B1234568"));
		assertThat(subject.getSexCode(), equalTo("F"));
	}
	
	final String CRIMINAL_INITIAL_RESULTS_UPDATE = "update rapback_datastore.CRIMINAL_INITIAL_RESULTS "
	        + "set SEARCH_RESULT_FILE = ? where TRANSACTION_NUMBER = ? and RESULTS_SENDER_ID = ?";
	
	@Test
	public void testGetCriminalInitialResults() throws Exception {
	    byte[] fbiCriminalResults = ZipUtils.zip("FBICriminalHistory".getBytes());
        jdbcTemplate.update(CRIMINAL_INITIAL_RESULTS_UPDATE, fbiCriminalResults, "000001820140729014008339994", 1);
	    
        byte[] stateCriminalResults = ZipUtils.zip("StateCriminalHistory".getBytes());
        jdbcTemplate.update(CRIMINAL_INITIAL_RESULTS_UPDATE, stateCriminalResults, "000001820140729014008339994", 2);
        
		List<CriminalInitialResults> criminalInitialResults= 
				rapbackDAO.getIdentificationCriminalInitialResults("000001820140729014008339994");
		log.info("Criminal Initial Results count: " + criminalInitialResults.size());
		assertEquals(2, criminalInitialResults.size());
		log.info("Search result doc content: " + new String(criminalInitialResults.get(0).getSearchResultFile()));
		assertEquals(18, criminalInitialResults.get(0).getSearchResultFile().length);
		
		assertNotNull(criminalInitialResults.get(0).getIdentificationTransaction());
		assertNotNull(criminalInitialResults.get(1).getIdentificationTransaction());
		assertTrue(criminalInitialResults.get(0).getIdentificationTransaction().getSubject().equals(
				criminalInitialResults.get(1).getIdentificationTransaction().getSubject()));
		IdentificationTransaction identificationTransaction = criminalInitialResults.get(0).getIdentificationTransaction();
		assertThat(identificationTransaction.getOwnerAgencyName(), equalTo("Demo Agency"));
		
		Subject subject = criminalInitialResults.get(0).getIdentificationTransaction().getSubject(); 
		log.info("Subject: " + subject);

		assertTrue(DateUtils.isSameDay(subject.getDob().toDate(),  DateTime.parse("1987-10-10").toDate()));
		assertThat(subject.getUcn(), equalTo("B1234569"));
		assertThat(subject.getCriminalSid(), equalTo("C1234568"));
		assertThat(subject.getCivilSid(), equalTo("A123458"));
		assertThat(subject.getFirstName(), equalTo("Bart"));
		assertThat(subject.getSexCode(), equalTo("M"));

	}
	
	final String RAPBACK_DATA_STORE_UPDATE = "update rapback_datastore.subsequent_results "
	        + "SET RAP_SHEET = ? where TRANSACTION_NUMBER = ? and RESULTS_SENDER_ID = ?";
	
	@Test
	public void testGetSubsequentResults() throws Exception {
		byte[] fbiSubResults = ZipUtils.zip("FBI Subsequent results.".getBytes());
		jdbcTemplate.update(RAPBACK_DATA_STORE_UPDATE, fbiSubResults, "000001820140729014008339995", 1);
		byte[] stateSubResults = ZipUtils.zip("State Subsequent results.".getBytes());
		jdbcTemplate.update(RAPBACK_DATA_STORE_UPDATE, stateSubResults, "000001820140729014008339995", 2);
	    
		List<SubsequentResults> subsequentResults = rapbackDAO.getSubsequentResults("000001820140729014008339995");
		assertEquals(2, subsequentResults.size());
		SubsequentResults result1 = subsequentResults.get(0);
		assertEquals("9222201", result1.getUcn());
		assertEquals(ResultSender.FBI, result1.getResultsSender());
		
		SubsequentResults result2 = subsequentResults.get(1);
		assertEquals(Long.valueOf(2), result2.getId());
		assertNull(result2.getUcn());
		assertEquals("A123459", result2.getCivilSid());
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
		assertEquals(Boolean.TRUE, agencyProfile.getStateSubscriptionQualified());
		assertEquals("demo.agency@localhost", agencyProfile.getEmails().get(0));
		assertEquals("demo.agency2@localhost", agencyProfile.getEmails().get(1));
		
		assertThat(agencyProfile.getTriggeringEventCodes().size(), equalTo(4));
		assertTrue(agencyProfile.getTriggeringEventCodes()
				.containsAll(Arrays.asList("ARREST", "DISPOSITION", "NCIC-WARRANT", "NCIC-SOR")));
		
		AgencyProfile agencyProfileNull = rapbackDAO.getAgencyProfile("123456789");
		assertNull(agencyProfileNull);
	}
}
