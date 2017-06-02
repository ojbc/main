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
import static org.junit.Assert.assertNull;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Basic unit test for static validation due date strategy.
 *
 */
public class TestStaticValidationDueDateStrategy {

	@Test
	public void testStaticValidationDueDateStrategy() throws Exception
	{
		StaticValidationDueDateStrategy staticValidationDueDateStrategy = new StaticValidationDueDateStrategy("SYSTEM");
		
		DateTime currentDate = new DateTime();
		
		Subscription subscription = new Subscription();
		subscription.setLastValidationDate(currentDate);
		subscription.setSubscriptionOwner("SYSTEM");
		
		assertNull(staticValidationDueDateStrategy.getValidationDueDate(subscription));
		
		subscription.setSubscriptionOwner("NOT_SYSTEM");
		assertEquals(currentDate, staticValidationDueDateStrategy.getValidationDueDate(subscription));
		
		staticValidationDueDateStrategy.setValidDays(30);
		assertEquals(currentDate.plusDays(30), staticValidationDueDateStrategy.getValidationDueDate(subscription));
	}
	
}
