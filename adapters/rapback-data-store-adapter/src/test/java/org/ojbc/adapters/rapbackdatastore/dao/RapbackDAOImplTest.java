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
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFingerPrints;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialRapSheet;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalFingerPrints;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RapbackDAOImplTest {
    private static final String TRANSACTION_NUMBER = "000001820140729014008340000";

	private final Log log = LogFactory.getLog(this.getClass());
    
	@Autowired
	RapbackDAO rapbackDAO;
	
    @Resource  
    private DataSource dataSource;  

	@Before
	public void setUp() throws Exception {
		assertNotNull(rapbackDAO);
	}
	
	@Test
	public void testRapbackDatastore() throws Exception {
		
		Connection conn = dataSource.getConnection();
		ResultSet rs = conn.createStatement().executeQuery("select * from subscription");
		assertTrue(rs.next());
		assertEquals(62720,rs.getInt("id"));
	}

	@Test
	@DirtiesContext
	public void testSaveSubject() throws Exception {
		Subject subject = new Subject(); 
		subject.setUcn("B1234567");
		subject.setCivilSid("B1234567");
		subject.setDob(new DateTime(1990, 5, 12,0,0,0,0));
		subject.setFirstName("Homer");
		subject.setLastName("Simpson");
		subject.setMiddleInitial("W");
		subject.setSexCode("M");
		
		Integer subjectId = rapbackDAO.saveSubject(subject); 
		
		assertNotNull(subjectId);
		assertEquals(1, subjectId.intValue()); 
		
		Subject persistedSubject = rapbackDAO.getSubject(subjectId); 
		log.info(persistedSubject.toString());
		
		assertEquals(persistedSubject.toString(), "Subject[subjectId=1,ucn=B1234567,criminalSid=<null>,"
				+ "civilSid=B1234567,firstName=Homer,lastName=Simpson,middleInitial=W,"
				+ "dob=1990-05-12T00:00:00.000-05:00,sexCode=M]");
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
		
		Subject subject = new Subject(); 
		subject.setUcn("B1234567");
		subject.setCivilSid("A123456");
		subject.setDob(new DateTime(1990, 5, 12,0,0,0,0));
		subject.setFirstName("Homer");
		subject.setLastName("Simpson");
		subject.setMiddleInitial("W");
		
		transaction.setSubject(subject);
		
		rapbackDAO.saveIdentificationTransaction(transaction);
	}

	@Test
	@DirtiesContext
	public void testSaveIdentificationTransactionWithOutSubject() throws Exception {
		IdentificationTransaction transaction = new IdentificationTransaction(); 
		transaction.setTransactionNumber(TRANSACTION_NUMBER);
		transaction.setOtn("12345");
		transaction.setOwnerOri("68796860");
		transaction.setOwnerProgramOca("ID23457");
		
		rapbackDAO.saveIdentificationTransaction(transaction); 
		
	}
	
	@Test
	@DirtiesContext
	public void testSaveCivilFingerPrints() throws Exception {
		
		saveIdentificationTransaction();
		
		CivilFingerPrints civilFingerPrints = new CivilFingerPrints(); 
		civilFingerPrints.setTransactionNumber(TRANSACTION_NUMBER);
		civilFingerPrints.setFingerPrintsFile("FingerPrints".getBytes());
		civilFingerPrints.setTransactionType("Transaction Type");
		civilFingerPrints.setFingerPrintsType("FBI");
		
		Integer pkId = rapbackDAO.saveCivilFingerPrints(civilFingerPrints);
		assertNotNull(pkId);
		assertEquals(1, pkId.intValue()); 
	}
	
	@Test
	@DirtiesContext
	public void testSaveCriminalFingerPrints() throws Exception {
		saveIdentificationTransaction();
		
		CriminalFingerPrints criminalFingerPrints = new CriminalFingerPrints(); 
		criminalFingerPrints.setTransactionNumber(TRANSACTION_NUMBER);
		criminalFingerPrints.setFingerPrintsFile("FingerPrints".getBytes());
		criminalFingerPrints.setTransactionType("Transaction Type");
		criminalFingerPrints.setFingerPrintsType("FBI");
		
		Integer pkId = rapbackDAO.saveCriminalFingerPrints(criminalFingerPrints);
		assertNotNull(pkId);
		assertEquals(1, pkId.intValue()); 
	}
	
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
		criminalInitialResults.setMatch(Boolean.TRUE);
		criminalInitialResults.setTransactionType("Transaction Type");
		criminalInitialResults.setResultsSender("FBI");
		criminalInitialResults.setRapBackCategory("CAR");
	
		criminalInitialResults.setSubject(identificationTransaction.getSubject());
		Integer pkId = rapbackDAO.saveCriminalInitialResults(criminalInitialResults);
		assertNotNull(pkId);
		assertEquals(1, pkId.intValue()); 
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
		civilInitialResults.setMatch(Boolean.TRUE);
		civilInitialResults.setCurrentState("current_state");
		civilInitialResults.setTransactionType("Transaction Type");
		civilInitialResults.setCivilRapBackCategory("I");
		civilInitialResults.setResultsSender("FBI");
		
		civilInitialResults.setSubject(identificationTransaction.getSubject());
		Integer pkId = rapbackDAO.saveCivilInitialResults(civilInitialResults);
		assertNotNull(pkId);
		assertEquals(1, pkId.intValue()); 
		
		CivilInitialRapSheet civilInitialRapSheet = new CivilInitialRapSheet();
		civilInitialRapSheet.setCivilIntitialResultId(1);
		civilInitialRapSheet.setRapSheet("rapsheet".getBytes());
		civilInitialRapSheet.setTransactionType("Transaction Type");
		
		Integer civilInitialRapSheetPkId = 
				rapbackDAO.saveCivilInitialRapSheet(civilInitialRapSheet);  
		assertNotNull(civilInitialRapSheetPkId);
		assertEquals(1, civilInitialRapSheetPkId.intValue()); 
	}
	
}
