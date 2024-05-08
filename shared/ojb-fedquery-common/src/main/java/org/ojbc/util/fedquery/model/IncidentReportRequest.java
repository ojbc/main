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
package org.ojbc.util.fedquery.model;

public class IncidentReportRequest {
	private String id;
	private String categoryCode;
	private String systemNameText;
	private String sourceSystemNameText;
	private String incidentNumber;
	
	public String getIncidentNumber() {
		return incidentNumber;
	}
	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}
	public String getSourceSystemNameText() {
		return sourceSystemNameText;
	}
	public void setSourceSystemNameText(String sourceSystemNameText) {
		this.sourceSystemNameText = sourceSystemNameText;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getSystemNameText() {
		return systemNameText;
	}
	public void setSystemNameText(String systemNameText) {
		this.systemNameText = systemNameText;
	}
	
	@Override
	public String toString() {
		return "IncidentReportRequest [id=" + id + ", categoryCode=" + categoryCode + ", systemNameText="
				+ systemNameText + "]";
	}
	
}
