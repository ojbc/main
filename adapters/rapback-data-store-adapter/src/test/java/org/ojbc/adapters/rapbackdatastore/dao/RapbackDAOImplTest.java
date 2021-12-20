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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.adapters.rapbackdatastore.RapbackDataStoreAdapterConstants;
import org.ojbc.adapters.rapbackdatastore.application.RapbackDatastoreAdapterApplication;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFingerPrints;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialRapSheet;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalHistoryDemographicsUpdateRequest;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.FingerPrintsType;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.NsorDemographics;
import org.ojbc.adapters.rapbackdatastore.dao.model.NsorSearchResult;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.model.rapback.IdentificationTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@CamelSpringBootTest
@SpringBootTest(classes=RapbackDatastoreAdapterApplication.class)
@ActiveProfiles("dev")
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
        "classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
		})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
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

    //This is used to update database to achieve desired state for test
    private JdbcTemplate jdbcTemplate;
	
	@BeforeEach
	public void setUp() throws Exception {
		assertNotNull(rapbackDAO);
		assertNotNull(fbiSubscriptionDao);
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
    	//We will use these subscriptions for our tests, update their validation dates so they aren't filtered out
    	int rowsUpdated = this.jdbcTemplate.update("update subscription set validationDueDate = curdate()");
    	assertEquals(11, rowsUpdated);
    	
		List<String> result = jdbcTemplate.queryForList("select table_name from information_schema.tables",String.class); 
		assertNotNull(result);
	}
	
	@Test
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
		
		Subject persistedSubject = rapbackDAO.getSubject(subjectId); 
		log.info(persistedSubject.toString());
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
	public void testSaveIdentificationTransactionWithSubject() throws Exception {
		saveIdentificationTransaction(TRANSACTION_NUMBER + "1"); 
		IdentificationTransaction identificationTransaction = 
				rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER + "1"); 
		assertNotNull(identificationTransaction); 
		assertNotNull(identificationTransaction.getSubject()); 
		log.info(identificationTransaction.toString());
	}

	private void saveIdentificationTransaction(String transactionNumber) {
		IdentificationTransaction transaction = new IdentificationTransaction(); 
		transaction.setTransactionNumber(transactionNumber);
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

	@Test
	public void testSaveIdentificationTransactionWithoutSubject() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> { IdentificationTransaction transaction = new IdentificationTransaction(); 
		transaction.setTransactionNumber(TRANSACTION_NUMBER);
		transaction.setOtn("12345");
		transaction.setOwnerOri("68796860");
		transaction.setOwnerProgramOca("ID23457");
		transaction.setIdentificationCategory("CAR");
		transaction.setCurrentState(IdentificationTransactionState.Available_for_Subscription);
		
		rapbackDAO.saveIdentificationTransaction(transaction);
		});
		
	}
	
	@Test
	public void testSaveCivilFingerPrints() throws Exception {
		
		saveIdentificationTransaction(TRANSACTION_NUMBER + "2");
		
		CivilFingerPrints civilFingerPrints = new CivilFingerPrints(); 
		civilFingerPrints.setTransactionNumber(TRANSACTION_NUMBER + "2");
		civilFingerPrints.setFingerPrintsFile("FingerPrints".getBytes());
		civilFingerPrints.setFingerPrintsType(FingerPrintsType.State);
		
		Integer pkId = rapbackDAO.saveCivilFingerPrints(civilFingerPrints);
		assertNotNull(pkId);
		
	}
	
//	@Test
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
	public void testSaveCriminalInitialResults() throws Exception {
		saveIdentificationTransaction(TRANSACTION_NUMBER + "3");
		
		IdentificationTransaction identificationTransaction = 
				rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER + "3"); 
		
		assertNotNull(identificationTransaction); 
		assertNotNull(identificationTransaction.getSubject()); 
		
		CriminalInitialResults criminalInitialResults = new CriminalInitialResults(); 
		criminalInitialResults.setTransactionNumber(TRANSACTION_NUMBER + "3");
		criminalInitialResults.setSearchResultFile("Match".getBytes());
		criminalInitialResults.setResultsSender(ResultSender.FBI);
	
		criminalInitialResults.setSubject(identificationTransaction.getSubject());
		Integer pkId = rapbackDAO.saveCriminalInitialResults(criminalInitialResults);
		assertNotNull(pkId);
	}
	
	@Test
	public void testSaveCivilInitialResults() throws Exception {
		saveIdentificationTransaction(TRANSACTION_NUMBER + "4");
		
		IdentificationTransaction identificationTransaction = 
				rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER + "4"); 
		
		assertNotNull(identificationTransaction); 
		assertNotNull(identificationTransaction.getSubject()); 
		
		CivilInitialResults civilInitialResults = new CivilInitialResults(); 
		civilInitialResults.setTransactionNumber(TRANSACTION_NUMBER + "4");
		civilInitialResults.setSearchResultFile("Match".getBytes());
		civilInitialResults.setResultsSender(ResultSender.FBI);
		
		Integer pkId = rapbackDAO.saveCivilInitialResults(civilInitialResults);
		assertNotNull(pkId);
		
		CivilInitialResults persistedCivilInitialResults = 
				(rapbackDAO.getCivilInitialResults(identificationTransaction.getOwnerOri())).get(2);
		log.info("PersistedCivilIntialResults: \n" + persistedCivilInitialResults.toString());
		
		
		CivilInitialRapSheet civilInitialRapSheet = new CivilInitialRapSheet();
		civilInitialRapSheet.setCivilIntitialResultId(11);
		civilInitialRapSheet.setRapSheet("rapsheet".getBytes());
		
		Integer civilInitialRapSheetPkId = 
				rapbackDAO.saveCivilInitialRapSheet(civilInitialRapSheet);  
		assertNotNull(civilInitialRapSheetPkId);
	}
	
	@Test
	public void testSaveNsorInfo() throws Exception {
		
		saveIdentificationTransaction(TRANSACTION_NUMBER + "5");
		
		IdentificationTransaction identificationTransaction = 
				rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER + "5"); 
		
		assertNotNull(identificationTransaction); 
		assertNotNull(identificationTransaction.getSubject());		
		
		NsorDemographics nsorDemographics = new NsorDemographics();
		nsorDemographics.setTransactionNumber(TRANSACTION_NUMBER + "5");
		nsorDemographics.setDemographicsFile("Demographics File Info".getBytes());
		nsorDemographics.setResultsSender(ResultSender.FBI);
		
		Integer pkId = rapbackDAO.saveNsorDemographics(nsorDemographics);
		assertNotNull(pkId);
		
		List<NsorDemographics> nsorDemographicsList = rapbackDAO.getNsorDemographics(TRANSACTION_NUMBER + "5");
		assertEquals(1, nsorDemographicsList.size());
		assertEquals(TRANSACTION_NUMBER + "5", nsorDemographicsList.get(0).getTransactionNumber());
		assertTrue(Arrays.equals("Demographics File Info".getBytes(), nsorDemographicsList.get(0).getDemographicsFile()));
		
		NsorSearchResult nsorSearchResult = new NsorSearchResult();
		nsorSearchResult.setTransactionNumber(TRANSACTION_NUMBER + "5");
		nsorSearchResult.setSearchResultFile("Search Results File Info".getBytes());
		nsorSearchResult.setResultsSender(ResultSender.FBI);

		pkId = rapbackDAO.saveNsorSearchResult(nsorSearchResult);
		assertNotNull(pkId);
		
		List<NsorSearchResult> nsorSearchResults = rapbackDAO.getNsorSearchResults(TRANSACTION_NUMBER + "5");
		assertEquals(1, nsorSearchResults.size());
		assertEquals(TRANSACTION_NUMBER + "5", nsorSearchResults.get(0).getTransactionNumber());
		assertTrue(Arrays.equals("Search Results File Info".getBytes(), nsorSearchResults.get(0).getSearchResultFile()));

	}

	@Test
	public void testSaveAndUpdateFbiSubscription() throws Exception {
		FbiRapbackSubscription fbiRapbackSubscription = new FbiRapbackSubscription(); 
		fbiRapbackSubscription.setFbiSubscriptionId("12345");
		fbiRapbackSubscription.setRapbackActivityNotificationFormat("2");
		fbiRapbackSubscription.setRapbackCategory("CI");
		fbiRapbackSubscription.setRapbackExpirationDate(LocalDate.of(2016, 5, 12));
		fbiRapbackSubscription.setRapbackStartDate(LocalDate.of(2014, 5, 12));
		fbiRapbackSubscription.setRapbackTermDate(LocalDate.of(2016, 5, 12));
		fbiRapbackSubscription.setRapbackOptOutInState(Boolean.FALSE);
		fbiRapbackSubscription.setSubscriptionTerm("2");
		fbiRapbackSubscription.setUcn("LI3456789");
		fbiRapbackSubscription.setStateSubscriptionId("62725");
		fbiRapbackSubscription.setEventIdentifier("eventID");
		
		rapbackDAO.saveFbiRapbackSubscription(fbiRapbackSubscription);
		
		FbiRapbackSubscription savedFbiRapbackSubscription = fbiSubscriptionDao.getFbiRapbackSubscription("CI", "LI3456789");
		log.info("savedFbiRapbackSubscription:  " + savedFbiRapbackSubscription.toString());
		assertEquals("12345", savedFbiRapbackSubscription.getFbiSubscriptionId());
		assertEquals("CI", savedFbiRapbackSubscription.getRapbackCategory());
		assertEquals("2", savedFbiRapbackSubscription.getSubscriptionTerm());
		assertEquals(LocalDate.of(2016, 5, 12), savedFbiRapbackSubscription.getRapbackExpirationDate());
		assertEquals(LocalDate.of(2016, 5, 12), savedFbiRapbackSubscription.getRapbackTermDate());
		assertEquals(LocalDate.of(2014, 5, 12), savedFbiRapbackSubscription.getRapbackStartDate());
		assertEquals(Boolean.FALSE, savedFbiRapbackSubscription.getRapbackOptOutInState());
		assertEquals("2", savedFbiRapbackSubscription.getRapbackActivityNotificationFormat());
		assertEquals("LI3456789", savedFbiRapbackSubscription.getUcn());
		assertThat(savedFbiRapbackSubscription.getStateSubscriptionId(), equalTo(62725)); 
		
		fbiRapbackSubscription.setRapbackActivityNotificationFormat("3");
		fbiRapbackSubscription.setSubscriptionTerm("5");
		fbiRapbackSubscription.setRapbackTermDate(LocalDate.of(2019, 5, 12));
		rapbackDAO.updateFbiRapbackSubscription(fbiRapbackSubscription);
		
		FbiRapbackSubscription updatedFbiRapbackSubscription = fbiSubscriptionDao.getFbiRapbackSubscription("CI", "LI3456789");
		log.info("savedFbiRapbackSubscription:  " + savedFbiRapbackSubscription.toString());
		assertEquals("12345", updatedFbiRapbackSubscription.getFbiSubscriptionId());
		assertEquals("CI", updatedFbiRapbackSubscription.getRapbackCategory());
		assertEquals("5", updatedFbiRapbackSubscription.getSubscriptionTerm());
		assertEquals(LocalDate.of(2016, 5, 12), updatedFbiRapbackSubscription.getRapbackExpirationDate());
		assertEquals(LocalDate.of(2019, 5, 12), updatedFbiRapbackSubscription.getRapbackTermDate());
		assertEquals(LocalDate.of(2014, 5, 12), updatedFbiRapbackSubscription.getRapbackStartDate());
		assertEquals(Boolean.FALSE, updatedFbiRapbackSubscription.getRapbackOptOutInState());
		assertEquals("3", updatedFbiRapbackSubscription.getRapbackActivityNotificationFormat());
		assertEquals("LI3456789", updatedFbiRapbackSubscription.getUcn());
		assertThat(savedFbiRapbackSubscription.getStateSubscriptionId(), equalTo(62725)); 
	}
	
	@Test
	public void testSaveFbiSubscriptionError() throws Exception {
		assertThrows(DuplicateKeyException.class, () -> { FbiRapbackSubscription fbiRapbackSubscription = new FbiRapbackSubscription(); 
		fbiRapbackSubscription.setFbiSubscriptionId("fbiSubscriptionId");
		fbiRapbackSubscription.setRapbackActivityNotificationFormat("2");
		fbiRapbackSubscription.setRapbackCategory("CI");
		fbiRapbackSubscription.setRapbackExpirationDate(LocalDate.of(2016, 5, 12));
		fbiRapbackSubscription.setRapbackStartDate(LocalDate.of(2014, 5, 12));
		fbiRapbackSubscription.setRapbackTermDate(LocalDate.of(2016, 5, 12));
		fbiRapbackSubscription.setRapbackOptOutInState(Boolean.FALSE);
		fbiRapbackSubscription.setSubscriptionTerm("2");
		fbiRapbackSubscription.setUcn("LI3456789");
		fbiRapbackSubscription.setStateSubscriptionId(62725);
		
		rapbackDAO.saveFbiRapbackSubscription(fbiRapbackSubscription);
		});
		
	}

	@Test
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
	public void testArchiveIdentificationResult() throws Exception {
		Connection conn = dataSource.getConnection();
		String sql = "SELECT * "
				+ "FROM identification_transaction "
				+ "WHERE transaction_number = '000001820140729014008339997' ";
		
		ResultSet rs = conn.createStatement().executeQuery(sql);
		assertTrue(rs.next());
		assertEquals(true,rs.getBoolean("archived"));

		int count = rapbackDAO.unarchiveIdentificationResult("000001820140729014008339997");
		
		assertEquals(1, count);
		
		ResultSet rsAfter = conn.createStatement().executeQuery(sql);
		assertTrue(rsAfter.next());
		assertEquals(false, rsAfter.getBoolean("archived"));
		
		count = rapbackDAO.archiveIdentificationResult("000001820140729014008339997");
		
		assertEquals(1, count);
		
		rsAfter = conn.createStatement().executeQuery(sql);
		assertTrue(rsAfter.next());
		assertEquals(true, rsAfter.getBoolean("archived"));
	}
	
	@Test
	public void testConsolidateSid() throws Exception {
		Connection conn = dataSource.getConnection();
		ResultSet rs = conn.createStatement().executeQuery(COUNT_SID_A123458);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(COUNT_SID_A123457);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));

		rapbackDAO.consolidateSidFederal("A123457", "A123458");
		rs = conn.createStatement().executeQuery(COUNT_SID_A123457);
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(COUNT_SID_A123458);
		assertTrue(rs.next());
		assertEquals(2,rs.getInt("rowcount"));
	}
	
	@Test
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
		
		rapbackDAO.consolidateUcnFederal("9222201", "9222202");
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

	@Test
	public void testReturnMatchingCivilIdentifications() throws Exception{
		
		List<IdentificationTransaction> identificationTransactions = rapbackDAO.returnMatchingCivilIdentifications("0400025", "A123459");
		
		assertEquals(1, identificationTransactions.size());
		
		Integer subscriptionId = identificationTransactions.get(0).getSubscriptionId();
		assertNotNull(subscriptionId);
		
	}
	
	@Test
	public void testUpdateCriminalHistoryDemographics()
	{
		CriminalHistoryDemographicsUpdateRequest criminalHistoryDemographicsUpdateRequest = new CriminalHistoryDemographicsUpdateRequest();
		Integer subjectId = 1;
		
		Subject subject = rapbackDAO.getSubject(1);
		
		assertEquals("Test", subject.getFirstName());
		assertEquals("Jane", subject.getLastName());
		assertEquals("W", subject.getMiddleInitial());
		assertEquals("1990-10-12", subject.getDob().toLocalDate().toString());
		
		criminalHistoryDemographicsUpdateRequest.setPostUpdateGivenName("Joan");
		criminalHistoryDemographicsUpdateRequest.setPostUpdateSurName("Jett");
		criminalHistoryDemographicsUpdateRequest.setPostUpdateMiddleName("M");
		criminalHistoryDemographicsUpdateRequest.setPostUpdateDOB(LocalDate.now());
		
		rapbackDAO.updateCriminalHistoryDemographics(criminalHistoryDemographicsUpdateRequest, subjectId);
		
		subject = rapbackDAO.getSubject(1);
		
		assertEquals("Joan", subject.getFirstName());
		assertEquals("Jett", subject.getLastName());
		assertEquals("M", subject.getMiddleInitial());
		assertEquals(LocalDate.now().toString(), subject.getDob().toLocalDate().toString());
		
		criminalHistoryDemographicsUpdateRequest.setPostUpdateGivenName("Test");
		criminalHistoryDemographicsUpdateRequest.setPostUpdateSurName("Jane");
		criminalHistoryDemographicsUpdateRequest.setPostUpdateMiddleName("W");
		criminalHistoryDemographicsUpdateRequest.setPostUpdateDOB(LocalDate.of(1990, 10, 12));
		
		rapbackDAO.updateCriminalHistoryDemographics(criminalHistoryDemographicsUpdateRequest, subjectId);

		subject = rapbackDAO.getSubject(1);
		
		assertEquals("Test", subject.getFirstName());
		assertEquals("Jane", subject.getLastName());
		assertEquals("1990-10-12", subject.getDob().toLocalDate().toString());
		assertEquals("W", subject.getMiddleInitial());

	}
	
	@Test
	public void testGetCivilFingerprint() throws Exception {
		
	}	
}
