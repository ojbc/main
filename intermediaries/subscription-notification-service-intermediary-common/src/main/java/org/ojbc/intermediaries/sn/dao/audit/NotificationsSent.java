package org.ojbc.intermediaries.sn.dao.audit;

import java.util.List;

import org.ojbc.intermediaries.sn.dao.Subscription;

public class NotificationsSent {

	private long notificationSentId;
	
	private Subscription subscription;
	
	private List<NotificationMechanism> notificationMechanisms;
	
	private List<NotificationProperties> notificationProperties;

	public long getNotificationSentId() {
		return notificationSentId;
	}

	public void setNotificationSentId(long notificationSentId) {
		this.notificationSentId = notificationSentId;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public List<NotificationMechanism> getNotificationMechanisms() {
		return notificationMechanisms;
	}

	public void setNotificationMechanisms(
			List<NotificationMechanism> notificationMechanisms) {
		this.notificationMechanisms = notificationMechanisms;
	}

	public List<NotificationProperties> getNotificationProperties() {
		return notificationProperties;
	}

	public void setNotificationProperties(
			List<NotificationProperties> notificationProperties) {
		this.notificationProperties = notificationProperties;
	}

	
}
