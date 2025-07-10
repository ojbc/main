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
package org.ojbc.intermediaries.sn.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.util.helper.ZipUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@CamelSpringTest
@SpringJUnitConfig(locations={
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",		
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
		"classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
}) 
@DirtiesContext
public class RapbackDAOImplSaveMethodsTest {
	private final Log log = LogFactory.getLog(this.getClass());
	    
	@Resource
	private FbiRapbackDao rapbackDao;
	
    @Resource(name = "rapbackDataSource" )  
    private DataSource rapbackDataSource;  

	@BeforeEach
	public void setUp() throws Exception {
		assertNotNull(rapbackDao);
	}
	
	@Test
	public void testSaveSubsequentResults() throws Exception {
		
		SubsequentResults subsequentResults = new SubsequentResults();
		subsequentResults.setTransactionNumber("000001820140729014008339995");
		subsequentResults.setUcn("9222201");
		subsequentResults.setRapSheet("rapsheet".getBytes());
		subsequentResults.setResultsSender(ResultSender.FBI);
		subsequentResults.setNotificationIndicator(true);
		
		Integer pkId = rapbackDao.saveSubsequentResults(subsequentResults);
		assertNotNull(pkId);
		
		Connection conn = rapbackDataSource.getConnection();
		conn.createStatement().execute("use rapback_datastore");
		ResultSet rs = conn.createStatement().executeQuery("select * from SUBSEQUENT_RESULTS where SUBSEQUENT_RESULT_ID = " + pkId.toString());
		assertTrue(rs.next());
		assertEquals("9222201", rs.getString("ucn"));
		assertEquals("000001820140729014008339995", rs.getString("transaction_number"));
		assertEquals(Boolean.TRUE, rs.getBoolean("notification_indicator"));
		
		String rapsheetContent = new String(ZipUtils.unzip(rs.getBytes("RAP_SHEET")));
		log.info("Rap sheet content: " + rapsheetContent);
		assertEquals("rapsheet", rapsheetContent);
		
		Integer rowsDeleted = rapbackDao.deleteSubsequentResults(subsequentResults);
		assertEquals(new Integer(1), rowsDeleted);
	}

}
