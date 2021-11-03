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
package org.ojbc.adapters.analyticaldatastore.dao.model;

public class IncidentOffense {

	//pk
	private Integer incidentOffenseID;
	
	//fk
	private Integer incidentID;

	private String IncidentOffenseCode;
	
	private String incidentOffenseText;

	public Integer getIncidentOffenseID() {
		return incidentOffenseID;
	}

	public void setIncidentOffenseID(Integer incidentOffenseID) {
		this.incidentOffenseID = incidentOffenseID;
	}

	public Integer getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(Integer incidentID) {
		this.incidentID = incidentID;
	}

	public String getIncidentOffenseCode() {
		return IncidentOffenseCode;
	}

	public void setIncidentOffenseCode(String incidentOffenseCode) {
		IncidentOffenseCode = incidentOffenseCode;
	}

	public String getIncidentOffenseText() {
		return incidentOffenseText;
	}

	public void setIncidentOffenseText(String incidentOffenseText) {
		this.incidentOffenseText = incidentOffenseText;
	} 

}
