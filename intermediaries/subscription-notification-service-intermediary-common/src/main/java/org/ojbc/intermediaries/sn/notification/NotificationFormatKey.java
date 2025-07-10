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
package org.ojbc.intermediaries.sn.notification;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class NotificationFormatKey {

	private String subscribingSystemName;
	private String subscriptionCategoryCode;
	private String notifyingSystemName;
	
	public String getSubscribingSystemName() {
		return subscribingSystemName;
	}
	public void setSubscribingSystemName(String subscribingSystemName) {
		this.subscribingSystemName = subscribingSystemName;
	}
	
	public String getSubscriptionCategoryCode() {
		return subscriptionCategoryCode;
	}
	public void setSubscriptionCategoryCode(String subscriptionCategoryCode) {
		this.subscriptionCategoryCode = subscriptionCategoryCode;
	}
	public String getNotifyingSystemName() {
		return notifyingSystemName;
	}
	public void setNotifyingSystemName(String notifyingSystemName) {
		this.notifyingSystemName = notifyingSystemName;
	}	

	@Override
	 public String toString() {
	  return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE); 
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((notifyingSystemName == null) ? 0 : notifyingSystemName
						.hashCode());
		result = prime
				* result
				+ ((subscribingSystemName == null) ? 0 : subscribingSystemName
						.hashCode());
		result = prime
				* result
				+ ((subscriptionCategoryCode == null) ? 0
						: subscriptionCategoryCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificationFormatKey other = (NotificationFormatKey) obj;
		if (notifyingSystemName == null) {
			if (other.notifyingSystemName != null)
				return false;
		} else if (!notifyingSystemName.equals(other.notifyingSystemName))
			return false;
		if (subscribingSystemName == null) {
			if (other.subscribingSystemName != null)
				return false;
		} else if (!subscribingSystemName.equals(other.subscribingSystemName))
			return false;
		if (subscriptionCategoryCode == null) {
			if (other.subscriptionCategoryCode != null)
				return false;
		} else if (!subscriptionCategoryCode
				.equals(other.subscriptionCategoryCode))
			return false;
		return true;
	}
}
