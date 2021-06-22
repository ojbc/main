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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.model.rapback.Subscription;
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
public class RapbackDAOImplGetMethodsTest {
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
	public void testGetStateSubscriptions(){
	
		List<Subscription> stateSubList = rapbackDao.getStateSubscriptions("1234", "CI");
		
		assertNotNull(stateSubList);		
		
		assertFalse(stateSubList.isEmpty());		
	}
	
	
	@Test
	public void testGetFbiUcnIdFromSubIdAndReasonCode(){
		
		String personFbiUcnId = rapbackDao.getFbiUcnIdFromSubIdAndReasonCode("62727", "CI");
		
		assertEquals("1234", personFbiUcnId);
	}
	
	@Test
	public void testCountStateSubscriptions(){
		
		int stateSubCount = rapbackDao.countStateSubscriptions("1234", "CI");
		
		assertEquals(1, stateSubCount);
	}
	
	
	@Test
	public void testGetFbiSubscription() throws Exception {
		
		FbiRapbackSubscription fbiSubscription = rapbackDao.getFbiRapbackSubscription("CI", "123456789");
				
		String fbiSubId = fbiSubscription.getFbiSubscriptionId();		
		assertEquals("fbiSubscriptionId", fbiSubId);
		
		String format = fbiSubscription.getRapbackActivityNotificationFormat();
		assertEquals("2", format);
				
		String category = fbiSubscription.getRapbackCategory();	
		assertEquals("CI", category);
				
		boolean optOutInState = fbiSubscription.getRapbackOptOutInState();
		assertEquals(false, optOutInState);
		
		LocalDate expDate = fbiSubscription.getRapbackExpirationDate();
		String sExpDate = expDate.toString();
		assertEquals("2015-12-19", sExpDate);
		
		LocalDate startDate = fbiSubscription.getRapbackStartDate();
		String sStartDate = startDate.toString();
		assertEquals("2014-10-19", sStartDate);
				
		String term = fbiSubscription.getSubscriptionTerm();
		assertEquals("5", term);
			
		String ucn = fbiSubscription.getUcn();
		assertEquals("123456789", ucn);				
	}	
	
	@Test
	public void testGetCivilFingerprints()
	{
		byte[] fingerPrints = rapbackDao.getCivilFingerPrints("000001820140729014008339990");
		
		assertEquals("StateCivilFingerPrints", new String(fingerPrints, StandardCharsets.UTF_8));
	}
	
	@Test
	public void testGetFbiIds() throws Exception {
		List<String> fbiIds = rapbackDao.getFbiIds("A123459"); 
		assertEquals(1, fbiIds.size());
		assertEquals("9222201", fbiIds.get(0));
		
		List<String> fbiSubscriptionIdsEmpty = rapbackDao.getFbiIds("9222202");
		assertTrue(fbiSubscriptionIdsEmpty.isEmpty());
	}
	
	@Test
	@Ignore("This test works locally but not in a maven build")
	public void testGetFbiSubscriptionQualification() throws Exception {
		Boolean fbiSubscriptionQualificationByTransactionNumber = rapbackDao.getfbiSubscriptionQualification("000001820140729014008339993"); 
		assertEquals(Boolean.TRUE, fbiSubscriptionQualificationByTransactionNumber);
		
		Boolean fbiSubscriptionQualificationBySubscriptionId = rapbackDao.getfbiSubscriptionQualification(62723); 
		assertEquals(Boolean.TRUE, fbiSubscriptionQualificationBySubscriptionId);
		
		Boolean fbiSubscriptionQualification = rapbackDao.getfbiSubscriptionQualification(99999); 
		log.info("fbiSubscriptionQualification:" + BooleanUtils.isTrue(fbiSubscriptionQualification));
		assertNull(fbiSubscriptionQualification);
	}
}
