package org.ojbc.intermediaries.sn.dao.audit;

public class NotificationMechanism {

	private long notificationMechanismId;
	private long notificationsSentId;
	
	private String notificationRecipientType;
	private String notificationAddress;
	private String notificationMechansim;
	
	public String getNotificationRecipientType() {
		return notificationRecipientType;
	}
	public void setNotificationRecipientType(String notificationRecipientType) {
		this.notificationRecipientType = notificationRecipientType;
	}
	public String getNotificationAddress() {
		return notificationAddress;
	}
	public void setNotificationAddress(String notificationAddress) {
		this.notificationAddress = notificationAddress;
	}
	public String getNotificationMechansim() {
		return notificationMechansim;
	}
	public void setNotificationMechansim(String notificationMechansim) {
		this.notificationMechansim = notificationMechansim;
	}
	public long getNotificationMechanismId() {
		return notificationMechanismId;
	}
	public void setNotificationMechanismId(long notificationMechanismId) {
		this.notificationMechanismId = notificationMechanismId;
	}
	public long getNotificationsSentId() {
		return notificationsSentId;
	}
	public void setNotificationsSentId(long notificationsSentId) {
		this.notificationsSentId = notificationsSentId;
	}
}
