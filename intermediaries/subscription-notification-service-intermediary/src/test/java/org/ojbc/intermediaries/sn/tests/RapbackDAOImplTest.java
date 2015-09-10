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
package org.ojbc.intermediaries.sn.tests;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.camel.test.junit4.CamelSpringJUnit4ClassRunner;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.sn.fbi.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.fbi.rapback.FbiRapbackSubscription;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations={
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/email-formatters.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/extensible-beans.xml",
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/search-query-routes.xml",
		"classpath:META-INF/spring/subscription-secure-routes.xml",
		"classpath:META-INF/spring/local-osgi-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",		
		"classpath:META-INF/spring/h2-mock-database-context-subscription.xml",
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
}) 
public class RapbackDAOImplTest {
	    
	@Resource
	private FbiRapbackDao rapbackDao;
	
    @Resource  
    private DataSource rapbackDataSource;  

	@Before
	public void setUp() throws Exception {
		assertNotNull(rapbackDao);
	}
	
	@Test
	@DirtiesContext
	public void testGetFbiSubscription() throws Exception {
		
		FbiRapbackSubscription fbiSubscription = rapbackDao.getFbiRapbackSubscription("CI", "123456789");
				
		String fbiSubId = fbiSubscription.getFbiSubscriptionId();		
		Assert.assertEquals("fbiSubscriptionId", fbiSubId);
		
		String format = fbiSubscription.getRapbackActivityNotificationFormat();
		Assert.assertEquals("email", format);
				
		String category = fbiSubscription.getRapbackCategory();	
		Assert.assertEquals("CI", category);
				
		boolean optOutInState = fbiSubscription.getRapbackOptOutInState();
		Assert.assertEquals(false, optOutInState);
		
		DateTime expDate = fbiSubscription.getRapbackExpirationDate();
		String sExpDate = expDate.toString("yyyy-MM-dd");
		Assert.assertEquals("2015-12-19", sExpDate);
		
		DateTime startDate = fbiSubscription.getRapbackStartDate();
		String sStartDate = startDate.toString("yyyy-MM-dd");
		Assert.assertEquals("2014-10-19", sStartDate);
				
		String term = fbiSubscription.getSubscriptionTerm();
		Assert.assertEquals("subscription term", term);
			
		String ucn = fbiSubscription.getUcn();
		Assert.assertEquals("123456789", ucn);				
	}	
	
}
