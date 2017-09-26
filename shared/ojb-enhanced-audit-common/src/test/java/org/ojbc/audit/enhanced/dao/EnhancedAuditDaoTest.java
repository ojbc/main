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
package org.ojbc.audit.enhanced.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class EnhancedAuditDaoTest {
		
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
		
	@Before
	public void init(){
	
		Assert.assertNotNull(enhancedAuditDao);
	}
		
	@Test
	public void testFederalSubscriptionMethods() throws Exception
	{

		FederalRapbackSubscription federalRapbackSubscription = new FederalRapbackSubscription();
		
		federalRapbackSubscription.setTransactionControlReferenceIdentification("9999999");
		federalRapbackSubscription.setPathToRequestFile("/some/path/to/requestFile");
		federalRapbackSubscription.setRequestSentTimestamp(LocalDateTime.now());
		
		enhancedAuditDao.saveFederalRapbackSubscription(federalRapbackSubscription);

		FederalRapbackSubscription federalRapbackSubscriptionFromDatabase = enhancedAuditDao.retrieveFederalRapbackSubscriptionFromTCN("9999999");
		
		federalRapbackSubscription.setTransactionCategoryCode("ERRA");
		federalRapbackSubscription.setPathToResponseFile("/some/path/to/responseFile");
		federalRapbackSubscription.setResponseRecievedTimestamp(LocalDateTime.now());
		federalRapbackSubscription.setFederalRapbackSubscriptionId(federalRapbackSubscriptionFromDatabase.getFederalRapbackSubscriptionId());
		
		enhancedAuditDao.updateFederalRapbackSubscriptionWithResponse(federalRapbackSubscription);
		
		federalRapbackSubscriptionFromDatabase = enhancedAuditDao.retrieveFederalRapbackSubscriptionFromTCN("9999999");
		
		assertEquals("9999999", federalRapbackSubscriptionFromDatabase.getTransactionControlReferenceIdentification());
		assertEquals("/some/path/to/requestFile", federalRapbackSubscriptionFromDatabase.getPathToRequestFile());
		assertEquals("/some/path/to/responseFile", federalRapbackSubscriptionFromDatabase.getPathToResponseFile());
		assertEquals("ERRA", federalRapbackSubscriptionFromDatabase.getTransactionCategoryCode());
		
	}
}

