package org.ojbc.intermediaries.sn.dao;

/**
 * Data Access Object class for notifications.
 *
 */
public class Notification {

	private String notificationId;
	private String notificationSubjectIdentifier;
	
	public String getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}
	public String getNotificationSubjectIdentifier() {
		return notificationSubjectIdentifier;
	}
	public void setNotificationSubjectIdentifier(
			String notificationSubjectIdentifier) {
		this.notificationSubjectIdentifier = notificationSubjectIdentifier;
	}
	
}
