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
package org.ojbc.util.model.rapback;

import java.util.List;

public class ExpiringSubscriptionRequest {

	private int daysUntilExpiry;
	private List<String> oris;
	private String systemName;
	
	public ExpiringSubscriptionRequest() {
		super();
	}
	
	public ExpiringSubscriptionRequest(int daysUntilExpiry, List<String> oris) {
		this();
		this.daysUntilExpiry = daysUntilExpiry;
		this.oris = oris;
		this.systemName = "{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB";
	}

	public int getDaysUntilExpiry() {
		return daysUntilExpiry;
	}
	public void setDaysUntilExpiry(int daysUntilExpiry) {
		this.daysUntilExpiry = daysUntilExpiry;
	}
	public List<String> getOris() {
		return oris;
	}
	public void setOris(List<String> oris) {
		this.oris = oris;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	
}
