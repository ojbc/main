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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.util.helper.ZipUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",		
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
}) 
@DirtiesContext
public class RapbackDAOImplSaveMethodsTest {
	private final Log log = LogFactory.getLog(this.getClass());
	    
	@Resource
	private FbiRapbackDao rapbackDao;
	
    @Resource  
    private DataSource dataSource;  

	@Before
	public void setUp() throws Exception {
		assertNotNull(rapbackDao);
	}
	
	@Test
	public void testSaveSubsequentResults() throws Exception {
		
		SubsequentResults subsequentResults = new SubsequentResults();
		subsequentResults.setUcn("9222201");
		subsequentResults.setRapSheet("rapsheet".getBytes());
		subsequentResults.setResultsSender(ResultSender.FBI);
		
		Integer pkId = rapbackDao.saveSubsequentResults(subsequentResults);
		assertNotNull(pkId);
		
		Connection conn = dataSource.getConnection();
		ResultSet rs = conn.createStatement().executeQuery("select * from SUBSEQUENT_RESULTS where SUBSEQUENT_RESULT_ID = " + pkId.toString());
		assertTrue(rs.next());
		assertEquals("9222201", rs.getString("ucn"));
		
		String rapsheetContent = new String(ZipUtils.unzip(rs.getBytes("RAP_SHEET")));
		log.info("Rap sheet content: " + rapsheetContent);
		assertEquals("rapsheet", rapsheetContent);
	}

}
