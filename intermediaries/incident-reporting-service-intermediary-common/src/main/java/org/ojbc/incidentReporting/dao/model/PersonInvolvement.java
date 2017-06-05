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
package org.ojbc.incidentReporting.dao.model;

public class PersonInvolvement {

	private String incidentId;
	private String personInvolvementHash;
	private String incidentOriginatingSystemUri;
	
	public String getIncidentId() {
		return incidentId;
	}
	public void setIncidentId(String incidentId) {
		this.incidentId = incidentId;
	}
	public String getPersonInvolvementHash() {
		return personInvolvementHash;
	}
	public void setPersonInvolvementHash(String personInvolvementHash) {
		this.personInvolvementHash = personInvolvementHash;
	}
	public String getIncidentOriginatingSystemUri() {
		return incidentOriginatingSystemUri;
	}
	public void setIncidentOriginatingSystemUri(String incidentOriginatingSystemUri) {
		this.incidentOriginatingSystemUri = incidentOriginatingSystemUri;
	}
	
}
