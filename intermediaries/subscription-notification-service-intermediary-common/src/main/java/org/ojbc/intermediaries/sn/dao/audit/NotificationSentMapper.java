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
