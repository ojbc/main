package org.ojbc.intermediaries.sn.dao.audit;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class NotificationMechanismMapper implements RowMapper<NotificationMechanism> {

	@Override
	public NotificationMechanism mapRow(ResultSet rs, int rowNum) throws SQLException {

		NotificationMechanism notificationMechanism = new NotificationMechanism();
		
		notificationMechanism.setNotificationAddress(rs.getString("NOTIFICATION_ADDRESS"));
		notificationMechanism.setNotificationMechansim(rs.getString("NOTIFICATION_MECHANSIM"));
		notificationMechanism.setNotificationRecipientType(rs.getString("NOTIFICATION_RECIPIENT_TYPE"));
		notificationMechanism.setNotificationsSentId(rs.getLong("NOTIFICATIONS_SENT_ID"));
		
		return notificationMechanism;
	
	}


}
