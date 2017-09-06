package org.ojbc.intermediaries.sn.dao.audit;

import org.ojbc.intermediaries.sn.notification.EmailNotification;

public interface AuditDAO {

	public Integer saveNotificationLogEntry(EmailNotification emailNotification);
	
	public Integer deleteNotificationLogEntry(Integer notificationSentId);
	
	public NotificationsSent retrieveNotificationSentById(Integer notificationSentId);
	
}
