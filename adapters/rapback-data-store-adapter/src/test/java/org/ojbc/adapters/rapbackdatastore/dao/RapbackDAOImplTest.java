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
		
		Integer subjectId = rapbackDAO.saveSubject(subject); 
		
		assertNotNull(subjectId);
		assertEquals(1, subjectId.intValue()); 
		
		Subject persistedSubject = rapbackDAO.getSubject(subjectId); 
		log.info(persistedSubject.toString());
		
		assertEquals(persistedSubject.toString(), "Subject[subjectId=1,ucn=B1234567,criminalSid=<null>,"
				+ "civilSid=B1234567,firstName=Homer,lastName=Simpson,middleInitial=W,dob=1990-05-12T00:00:00.000-05:00]");
	}

	@Test
	@DirtiesContext
	public void testSaveIdentificationTransaction() throws Exception {
		IdentificationTransaction transaction = new IdentificationTransaction(); 
		transaction.setTransactionNumber("000001820140729014008340000");
		transaction.setOtn("12345");
		transaction.setOwnerOri("68796860");
		transaction.setOwnerProgramOca("owner-program-oca");
		
		Subject subject = new Subject(); 
		subject.setUcn("B1234567");
		subject.setCivilSid("B1234567");
		subject.setDob(new DateTime(1990, 5, 12,0,0,0,0));
		subject.setFirstName("Homer");
		subject.setLastName("Simpson");
		subject.setMiddleInitial("W");
		
		transaction.setSubject(subject);
		
		rapbackDAO.saveIdentificationTransaction(transaction); 
		
	}

}
