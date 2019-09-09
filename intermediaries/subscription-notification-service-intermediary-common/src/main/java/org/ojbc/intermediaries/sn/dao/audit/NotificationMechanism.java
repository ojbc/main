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
