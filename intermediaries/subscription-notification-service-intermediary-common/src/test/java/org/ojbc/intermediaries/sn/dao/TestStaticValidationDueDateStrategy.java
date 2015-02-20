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
