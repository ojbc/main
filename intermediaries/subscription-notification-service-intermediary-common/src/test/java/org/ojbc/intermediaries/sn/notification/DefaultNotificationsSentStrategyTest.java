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
package org.ojbc.intermediaries.sn.notification;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

public class DefaultNotificationsSentStrategyTest {

	@Test
	public void testHasNotificationBeenSent()
	{
		DefaultNotificationsSentStrategy defaultNotificationsSentStrategy = new DefaultNotificationsSentStrategy();
		
		//In default state, we should return false because timestamp has never been set
		assertFalse(defaultNotificationsSentStrategy.hasNotificationBeenSent());
		
		//Update timestamp and then check if notification is sent
		defaultNotificationsSentStrategy.updateNotificationSentTimestamp();
		assertTrue(defaultNotificationsSentStrategy.hasNotificationBeenSent());

		//Default hours between notifications is 24, test with notification sent 23 hours ago
		defaultNotificationsSentStrategy.setLastNotificationSentTimestamp(LocalDateTime.now().minusHours(23));
		assertTrue(defaultNotificationsSentStrategy.hasNotificationBeenSent());

		//Default hours between notifications is 24, test with notification sent 24 hours ago
		defaultNotificationsSentStrategy.setLastNotificationSentTimestamp(LocalDateTime.now().minusHours(24));
		assertFalse(defaultNotificationsSentStrategy.hasNotificationBeenSent());
		
		//Test with non default value
		defaultNotificationsSentStrategy.setMaxHoursBetweenNotifications(10);

		//Test with notification sent 9 hours ago
		defaultNotificationsSentStrategy.setLastNotificationSentTimestamp(LocalDateTime.now().minusHours(9));
		assertTrue(defaultNotificationsSentStrategy.hasNotificationBeenSent());
		
		//Test with notification sent 10 hours ago
		defaultNotificationsSentStrategy.setLastNotificationSentTimestamp(LocalDateTime.now().minusHours(10));
		assertFalse(defaultNotificationsSentStrategy.hasNotificationBeenSent());

	}
	
}
