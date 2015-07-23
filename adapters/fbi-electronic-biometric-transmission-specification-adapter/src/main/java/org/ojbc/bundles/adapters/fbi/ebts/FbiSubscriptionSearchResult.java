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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.bundles.adapters.fbi.ebts;

import java.util.Date;

public class FbiSubscriptionSearchResult {
		 
	
	private Date startDate;
	
	private Date endDate;
	
	private String fbiId;
	
	private String reasonCode;
	
	private String termDuration;

		
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFbiId() {
		return fbiId;
	}

	public void setFbiId(String fbiId) {
		this.fbiId = fbiId;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getTermDuration() {
		return termDuration;
	}

	public void setTermDuration(String termDuration) {
		this.termDuration = termDuration;
	}

	@Override
	public String toString() {
		return "FbiSubscriptionSearchResult [startDate=" + startDate
				+ ", endDate=" + endDate + ", fbiId=" + fbiId + ", reasonCode="
				+ reasonCode + ", termDuration=" + termDuration + "]";
	}
		
}
