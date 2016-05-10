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
package org.ojbc.adapters.rapbackdatastore.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.rapbackdatastore.RapbackDataStoreAdapterConstants;
import org.ojbc.adapters.rapbackdatastore.dao.model.AgencyProfile;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFingerPrints;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialRapSheet;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.FingerPrintsType;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransactionState;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackSubscription;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.test.util.SAMLTokenTestUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
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
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class RapbackDAOImplTest {
    private static final String COUNT_SID_A123457 = "select count(*) as rowcount from identification_subject "
    		+ "where civil_sid = 'A123457' or criminal_Sid = 'A123457'";

	private static final String COUNT_SID_A123458 = "select count(*) as rowcount from identification_subject "
    		+ "where civil_sid = 'A123458' or criminal_Sid = 'A123458'";

    @Value("${rapbackDatastoreAdapter.civilIdlePeriod:60}")
    private Integer civilIdlePeriod;
    
	private static final String TRANSACTION_NUMBER = "000001820140729014008340000";

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
	@DirtiesContext
	public void testSaveSubject() throws Exception {
		Subject subject = new Subject(); 
		subject.setUcn("B1234567");
		subject.setCivilSid("A123456");
		subject.setDob(new DateTime(1969, 5, 12,0,0,0,0,DateTimeZone.getDefault()));
		subject.setFirstName("Homer");
		subject.setLastName("Simpson");
		subject.setMiddleInitial("W");
		subject.setSexCode("M");
		
		Integer subjectId = rapbackDAO.saveSubject(subject); 
		
		assertNotNull(subjectId);
		assertEquals(6, subjectId.intValue()); 
		
		Subject persistedSubject = rapbackDAO.getSubject(subjectId); 
		log.info(persistedSubject.toString());
		assertEquals(Integer.valueOf(6), persistedSubject.getSubjectId());
		assertEquals("1969-05-12", persistedSubject.getDob().toString("yyyy-MM-dd"));
		assertEquals("B1234567", persistedSubject.getUcn());
		assertNull(persistedSubject.getCriminalSid());
		assertEquals("A123456", persistedSubject.getCivilSid());
		assertEquals("Homer", persistedSubject.getFirstName());
		assertEquals("Simpson", persistedSubject.getLastName());
		assertEquals("W", persistedSubject.getMiddleInitial());
		assertEquals("M", persistedSubject.getSexCode());
		
	}

	@Test
	@DirtiesContext
	public void testSaveIdentificationTransactionWithSubject() throws Exception {
		saveIdentificationTransaction(); 
		IdentificationTransaction identificationTransaction = 
				rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER); 
		assertNotNull(identificationTransaction); 
		assertNotNull(identificationTransaction.getSubject()); 
		log.info(identificationTransaction.toString());
	}

	private void saveIdentificationTransaction() {
		IdentificationTransaction transaction = new IdentificationTransaction(); 
		transaction.setTransactionNumber(TRANSACTION_NUMBER);
		transaction.setOtn("12345");
		transaction.setOwnerOri("68796860");
		transaction.setOwnerProgramOca("ID23457");
		transaction.setIdentificationCategory("I");
		transaction.setArchived(Boolean.FALSE);
		
		Subject subject = new Subject(); 
		subject.setUcn("B1234567");
		subject.setCivilSid("A123456");
		subject.setDob(new DateTime(1969, 5, 12,0,0,0,0));
		subject.setFirstName("Homer");
		subject.setLastName("Simpson");
		subject.setMiddleInitial("W");
		subject.setSexCode("M");
		
		transaction.setSubject(subject);
		
		rapbackDAO.saveIdentificationTransaction(transaction);
	}

	@Test(expected=IllegalArgumentException.class)
	@DirtiesContext
	public void testSaveIdentificationTransactionWithoutSubject() throws Exception {
		IdentificationTransaction transaction = new IdentificationTransaction(); 
		transaction.setTransactionNumber(TRANSACTION_NUMBER);
		transaction.setOtn("12345");
		transaction.setOwnerOri("68796860");
		transaction.setOwnerProgramOca("ID23457");
		transaction.setIdentificationCategory("CAR");
		transaction.setCurrentState(IdentificationTransactionState.Available_for_subscription);
		
		rapbackDAO.saveIdentificationTransaction(transaction); 
		
	}
	
	@Test
	@DirtiesContext
	public void testSaveCivilFingerPrints() throws Exception {
		
		saveIdentificationTransaction();
		
		CivilFingerPrints civilFingerPrints = new CivilFingerPrints(); 
		civilFingerPrints.setTransactionNumber(TRANSACTION_NUMBER);
		civilFingerPrints.setFingerPrintsFile("FingerPrints".getBytes());
		civilFingerPrints.setFingerPrintsType(FingerPrintsType.FBI);
		
		Integer pkId = rapbackDAO.saveCivilFingerPrints(civilFingerPrints);
		assertNotNull(pkId);
		assertEquals(11, pkId.intValue()); 
	}
	
//	@Test
//	@DirtiesContext
//	public void testSaveCriminalFingerPrints() throws Exception {
//		saveIdentificationTransaction();
//		
//		CriminalFingerPrints criminalFingerPrints = new CriminalFingerPrints(); 
//		criminalFingerPrints.setTransactionNumber(TRANSACTION_NUMBER);
//		criminalFingerPrints.setFingerPrintsFile("FingerPrints".getBytes());
//		criminalFingerPrints.setTransactionType("Transaction Type");
//		criminalFingerPrints.setFingerPrintsType("FBI");
//		
//		Integer pkId = rapbackDAO.saveCriminalFingerPrints(criminalFingerPrints);
//		assertNotNull(pkId);
//		assertEquals(3, pkId.intValue()); 
//	}
	
	@Test
	@DirtiesContext
	public void testSaveCriminalInitialResults() throws Exception {
		saveIdentificationTransaction();
		
		IdentificationTransaction identificationTransaction = 
				rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER); 
		
		assertNotNull(identificationTransaction); 
		assertNotNull(identificationTransaction.getSubject()); 
		
		CriminalInitialResults criminalInitialResults = new CriminalInitialResults(); 
		criminalInitialResults.setTransactionNumber(TRANSACTION_NUMBER);
		criminalInitialResults.setSearchResultFile("Match".getBytes());
		criminalInitialResults.setResultsSender(ResultSender.FBI);
	
		criminalInitialResults.setSubject(identificationTransaction.getSubject());
		Integer pkId = rapbackDAO.saveCriminalInitialResults(criminalInitialResults);
		assertNotNull(pkId);
		assertEquals(5, pkId.intValue()); 
	}
	
	@Test
	@DirtiesContext
	public void testSaveCivilInitialResults() throws Exception {
		saveIdentificationTransaction();
		
		IdentificationTransaction identificationTransaction = 
				rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER); 
		
		assertNotNull(identificationTransaction); 
		assertNotNull(identificationTransaction.getSubject()); 
		
		CivilInitialResults civilInitialResults = new CivilInitialResults(); 
		civilInitialResults.setTransactionNumber(TRANSACTION_NUMBER);
		civilInitialResults.setSearchResultFile("Match".getBytes());
		civilInitialResults.setResultsSender(ResultSender.FBI);
		
		Integer pkId = rapbackDAO.saveCivilInitialResults(civilInitialResults);
		assertNotNull(pkId);
		assertEquals(11, pkId.intValue()); 
		
		CivilInitialResults persistedCivilInitialResults = 
				(rapbackDAO.getCivilInitialResults(identificationTransaction.getOwnerOri())).get(2);
		log.info("PersistedCivilIntialResults: \n" + persistedCivilInitialResults.toString());
		
		
		CivilInitialRapSheet civilInitialRapSheet = new CivilInitialRapSheet();
		civilInitialRapSheet.setCivilIntitialResultId(11);
		civilInitialRapSheet.setRapSheet("rapsheet".getBytes());
		
		Integer civilInitialRapSheetPkId = 
				rapbackDAO.saveCivilInitialRapSheet(civilInitialRapSheet);  
		assertNotNull(civilInitialRapSheetPkId);
		assertEquals(7, civilInitialRapSheetPkId.intValue()); 
	}
	
	@Test
	public void testGetCivilIdentificationTransactions() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser");
		customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Honolulu PD Records and ID Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "Sworn Supervisors");
		  
		SAMLTokenPrincipal samlAssertion = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
      
		List<IdentificationTransaction> transactions = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertion);
		assertEquals(4, transactions.size());
		
		customAttributes.put(SamlAttribute.EmployeePositionName, "Firearms Unit (both Civilian and Sworn)");
		SAMLTokenPrincipal samlAssertionWithOnlyOneViewableCategory = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
		
		List<IdentificationTransaction> transactions2 = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionWithOnlyOneViewableCategory);
		assertEquals(2, transactions2.size());
		
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:superuser");
		SAMLTokenPrincipal samlAssertionSuperUser = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
		
		List<IdentificationTransaction> transactionsForSuperUser = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionSuperUser);
		assertEquals(4, transactionsForSuperUser.size());
		
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:civilruser");
		customAttributes.put(SamlAttribute.EmployerORI, "68796860");
		SAMLTokenPrincipal samlAssertionCivilUser = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
		
		List<IdentificationTransaction> transactionsForCivilUser = 
				rapbackDAO.getCivilIdentificationTransactions(samlAssertionCivilUser);
		assertEquals(1, transactionsForCivilUser.size());
		
	}
	
	@Test
	public void testGetCriminalIdentificationTransactions() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser");
		customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Honolulu PD Records and ID Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "Sworn Supervisors");
		  
		SAMLTokenPrincipal samlAssertion = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
      
		List<IdentificationTransaction> transactions = 
				rapbackDAO.getCriminalIdentificationTransactions(samlAssertion);
		assertEquals(1, transactions.size());
		
		customAttributes.put(SamlAttribute.EmployeePositionName, "Firearms Unit (both Civilian and Sworn)");
		SAMLTokenPrincipal samlAssertionWithOnlyNoViewableCategory = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
		
		List<IdentificationTransaction> transactions2 = 
				rapbackDAO.getCriminalIdentificationTransactions(samlAssertionWithOnlyNoViewableCategory);
		assertEquals(0, transactions2.size());
		
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:superuser");
		SAMLTokenPrincipal samlAssertionSuperUser = SAMLTokenTestUtils.createSAMLTokenPrincipalWithAttributes(customAttributes);
		
		List<IdentificationTransaction> transactionsForSuperUser = 
				rapbackDAO.getCriminalIdentificationTransactions(samlAssertionSuperUser);
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
	public void testGetSubsequentResults() throws Exception {
		
		List<SubsequentResults> subsequentResults = rapbackDAO.getSubsequentResults("000001820140729014008339995");
		assertEquals(2, subsequentResults.size());
		SubsequentResults result1 = subsequentResults.get(0);
		assertEquals(Long.valueOf(1), result1.getId());
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
	@DirtiesContext
	public void testSaveAndUpdateFbiSubscription() throws Exception {
		FbiRapbackSubscription fbiRapbackSubscription = new FbiRapbackSubscription(); 
		fbiRapbackSubscription.setFbiSubscriptionId("12345");
		fbiRapbackSubscription.setRapbackActivityNotificationFormat("2");
		fbiRapbackSubscription.setRapbackCategory("CI");
		fbiRapbackSubscription.setRapbackExpirationDate(new DateTime(2016, 5, 12,0,0,0,0));
		fbiRapbackSubscription.setRapbackStartDate(new DateTime(2014, 5, 12,0,0,0,0));
		fbiRapbackSubscription.setRapbackTermDate(new DateTime(2016, 5, 12,0,0,0,0));
		fbiRapbackSubscription.setRapbackOptOutInState(Boolean.FALSE);
		fbiRapbackSubscription.setSubscriptionTerm("2");
		fbiRapbackSubscription.setUcn("LI3456789");
		
		rapbackDAO.saveFbiRapbackSubscription(fbiRapbackSubscription);
		
		FbiRapbackSubscription savedFbiRapbackSubscription = fbiSubscriptionDao.getFbiRapbackSubscription("CI", "LI3456789");
		log.info("savedFbiRapbackSubscription:  " + savedFbiRapbackSubscription.toString());
		assertEquals("12345", savedFbiRapbackSubscription.getFbiSubscriptionId());
		assertEquals("CI", savedFbiRapbackSubscription.getRapbackCategory());
		assertEquals("2", savedFbiRapbackSubscription.getSubscriptionTerm());
		assertEquals(new DateTime(2016, 5, 12, 0, 0, 0, 0, DateTimeZone.getDefault()), savedFbiRapbackSubscription.getRapbackExpirationDate());
		assertEquals(new DateTime(2016, 5, 12, 0, 0, 0, 0, DateTimeZone.getDefault()), savedFbiRapbackSubscription.getRapbackTermDate());
		assertEquals(new DateTime(2014, 5, 12, 0, 0, 0, 0, DateTimeZone.getDefault()), savedFbiRapbackSubscription.getRapbackStartDate());
		assertEquals(Boolean.FALSE, savedFbiRapbackSubscription.getRapbackOptOutInState());
		assertEquals("2", savedFbiRapbackSubscription.getRapbackActivityNotificationFormat());
		assertEquals("LI3456789", savedFbiRapbackSubscription.getUcn());
		
		fbiRapbackSubscription.setRapbackActivityNotificationFormat("3");
		fbiRapbackSubscription.setSubscriptionTerm("5");
		fbiRapbackSubscription.setRapbackTermDate(new DateTime(2019, 5, 12,0,0,0,0));
		rapbackDAO.updateFbiRapbackSubscription(fbiRapbackSubscription);
		
		FbiRapbackSubscription updatedFbiRapbackSubscription = fbiSubscriptionDao.getFbiRapbackSubscription("CI", "LI3456789");
		log.info("savedFbiRapbackSubscription:  " + savedFbiRapbackSubscription.toString());
		assertEquals("12345", updatedFbiRapbackSubscription.getFbiSubscriptionId());
		assertEquals("CI", updatedFbiRapbackSubscription.getRapbackCategory());
		assertEquals("5", updatedFbiRapbackSubscription.getSubscriptionTerm());
		assertEquals(new DateTime(2016, 5, 12, 0, 0, 0, 0, DateTimeZone.getDefault()), updatedFbiRapbackSubscription.getRapbackExpirationDate());
		assertEquals(new DateTime(2019, 5, 12, 0, 0, 0, 0, DateTimeZone.getDefault()), updatedFbiRapbackSubscription.getRapbackTermDate());
		assertEquals(new DateTime(2014, 5, 12, 0, 0, 0, 0, DateTimeZone.getDefault()), updatedFbiRapbackSubscription.getRapbackStartDate());
		assertEquals(Boolean.FALSE, updatedFbiRapbackSubscription.getRapbackOptOutInState());
		assertEquals("3", updatedFbiRapbackSubscription.getRapbackActivityNotificationFormat());
		assertEquals("LI3456789", updatedFbiRapbackSubscription.getUcn());
		
	}
	
	@Test(expected=DuplicateKeyException.class)
	@DirtiesContext
	public void testSaveFbiSubscriptionError() throws Exception {
		FbiRapbackSubscription fbiRapbackSubscription = new FbiRapbackSubscription(); 
		fbiRapbackSubscription.setFbiSubscriptionId("fbiSubscriptionId");
		fbiRapbackSubscription.setRapbackActivityNotificationFormat("2");
		fbiRapbackSubscription.setRapbackCategory("CI");
		fbiRapbackSubscription.setRapbackExpirationDate(new DateTime(2016, 5, 12,0,0,0,0));
		fbiRapbackSubscription.setRapbackStartDate(new DateTime(2014, 5, 12,0,0,0,0));
		fbiRapbackSubscription.setRapbackTermDate(new DateTime(2016, 5, 12,0,0,0,0));
		fbiRapbackSubscription.setRapbackOptOutInState(Boolean.FALSE);
		fbiRapbackSubscription.setSubscriptionTerm("2");
		fbiRapbackSubscription.setUcn("LI3456789");
		
		rapbackDAO.saveFbiRapbackSubscription(fbiRapbackSubscription);
		
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
	
	@Test
	@DirtiesContext
	public void testArchiveCivil() throws Exception {
		Connection conn = dataSource.getConnection();
		DateTime currentDate = new DateTime(); 
		DateTime comparableDate = currentDate.minusDays(civilIdlePeriod);
		String countQualifiedToArchiveSql = "SELECT count(*) as rowcount "
				+ "FROM identification_transaction t "
				+ "WHERE (select count(*)>0 FROM civil_initial_results c where c.transaction_number = t.transaction_number) "
				+ "AND archived = 'false' "
				+ "AND available_for_subscription_start_date < '" + comparableDate.toString(RapbackDataStoreAdapterConstants.YYYY_MM_DD) + "'";
		ResultSet rs = conn.createStatement().executeQuery(countQualifiedToArchiveSql);
		assertTrue(rs.next());
		
		int count = rs.getInt("rowcount");

		int archivedCount = rapbackDAO.archiveCivilIdentifications();
		assertEquals(count, archivedCount);
	}
	
	@Test
	@DirtiesContext
	public void testArchiveCriminal() throws Exception {
		Connection conn = dataSource.getConnection();
		DateTime currentDate = new DateTime(); 
		DateTime comparableDate = currentDate.minusDays(civilIdlePeriod);
		String countQualifiedToArchiveSql = "SELECT count(*) as rowcount "
				+ "FROM identification_transaction t "
				+ "WHERE (select count(*)>0 FROM criminal_initial_results c where c.transaction_number = t.transaction_number) "
				+ "AND archived = 'false' "
				+ "AND available_for_subscription_start_date < '" + comparableDate.toString(RapbackDataStoreAdapterConstants.YYYY_MM_DD) + "'";
		ResultSet rs = conn.createStatement().executeQuery(countQualifiedToArchiveSql);
		assertTrue(rs.next());
		
		int count = rs.getInt("rowcount");
		
		int archivedCount = rapbackDAO.archiveCriminalIdentifications();
		assertEquals(count, archivedCount);
	}
	
	@Test
	@DirtiesContext
	public void testArchiveIdentificationResult() throws Exception {
		Connection conn = dataSource.getConnection();
		String sql = "SELECT * "
				+ "FROM identification_transaction "
				+ "WHERE transaction_number = '000001820140729014008339997' ";
		
		ResultSet rs = conn.createStatement().executeQuery(sql);
		assertTrue(rs.next());
		assertEquals(false,rs.getBoolean("archived"));

		int count = rapbackDAO.archiveIdentificationResult("000001820140729014008339997");
		
		assertEquals(1, count);
		
		ResultSet rsAfter = conn.createStatement().executeQuery(sql);
		assertTrue(rsAfter.next());
		assertEquals(true, rsAfter.getBoolean("archived"));
	}
	
	@Test
	@DirtiesContext
	public void testConsolidateSid() throws Exception {
		Connection conn = dataSource.getConnection();
		ResultSet rs = conn.createStatement().executeQuery(COUNT_SID_A123458);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(COUNT_SID_A123457);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));

		rapbackDAO.consolidateSid("A123457", "A123458");
		rs = conn.createStatement().executeQuery(COUNT_SID_A123457);
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(COUNT_SID_A123458);
		assertTrue(rs.next());
		assertEquals(2,rs.getInt("rowcount"));
	}
	
	@Test
	@DirtiesContext
	public void testConsolidateUcn() throws Exception {
		Connection conn = dataSource.getConnection();
		String countSubjectUcn9222201 = "select count(*) as rowcount from identification_subject where ucn = '9222201'";
		ResultSet rs = conn.createStatement().executeQuery(countSubjectUcn9222201);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		
		String countSubjectUcn9222202 = "select count(*) as rowcount from identification_subject where ucn = '9222202'";
		rs = conn.createStatement().executeQuery(countSubjectUcn9222202);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));

		String countFbiSubscriptionUcn9222201 = "select count(*) as rowcount from fbi_rap_back_subscription where ucn = '9222201'";
		rs = conn.createStatement().executeQuery(countFbiSubscriptionUcn9222201);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		String countFbiSubscriptionUcn9222202 = "select count(*) as rowcount from fbi_rap_back_subscription where ucn = '9222202'";
		rs = conn.createStatement().executeQuery(countFbiSubscriptionUcn9222202);
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		
		rapbackDAO.consolidateUcn("9222201", "9222202");
		rs = conn.createStatement().executeQuery(countSubjectUcn9222201);
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(countSubjectUcn9222202);
		assertTrue(rs.next());
		assertEquals(2,rs.getInt("rowcount"));

		rs = conn.createStatement().executeQuery(countFbiSubscriptionUcn9222201);
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(countFbiSubscriptionUcn9222202);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
	}


}
