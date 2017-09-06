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
package org.ojbc.intermediaries.sn.dao.audit;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.intermediaries.sn.dao.Subscription;
import org.springframework.jdbc.core.RowMapper;

public class NotificationSentMapper implements RowMapper<NotificationsSent> {

	@Override
	public NotificationsSent mapRow(ResultSet rs, int rowNum) throws SQLException {

		NotificationsSent notificationsSent = new NotificationsSent();
		
		Subscription subscription = new Subscription();
		
		subscription.setOri(rs.getString("SUBSCRIPTION_OWNER_AGENCY"));
		subscription.setSubscriptionOwner(rs.getString("SUBSCRIPTION_OWNER"));
		subscription.setSubscriptionOwnerEmailAddress(rs.getString("SUBSCRIPTION_OWNER_EMAIL_ADDRESS"));
		subscription.setSubscribingSystemIdentifier(rs.getString("SUBSCRIBING_SYSTEM_IDENTIFIER"));
		subscription.setId(rs.getLong("SUBSCRIPTION_IDENTIFIER"));
		subscription.setTopic(rs.getString("TOPIC"));
		
		notificationsSent.setSubscription(subscription);
		
		return notificationsSent;
		
	}


}
