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
