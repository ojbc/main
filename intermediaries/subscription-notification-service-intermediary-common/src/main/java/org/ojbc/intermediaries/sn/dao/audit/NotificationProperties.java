package org.ojbc.intermediaries.sn.dao.audit;

public class NotificationProperties {

	private long notificationPropertiesEd;
	private long notificationsSentId;
	private String propertyValue;
	private String propertyName;
	
	public long getNotificationPropertiesEd() {
		return notificationPropertiesEd;
	}
	public void setNotificationPropertiesEd(long notificationPropertiesEd) {
		this.notificationPropertiesEd = notificationPropertiesEd;
	}
	public long getNotificationsSentId() {
		return notificationsSentId;
	}
	public void setNotificationsSentId(long notificationsSentId) {
		this.notificationsSentId = notificationsSentId;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}
