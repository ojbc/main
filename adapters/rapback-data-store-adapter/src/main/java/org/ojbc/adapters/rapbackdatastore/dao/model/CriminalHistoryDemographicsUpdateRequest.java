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
package org.ojbc.adapters.rapbackdatastore.dao.model;

import java.time.LocalDate;

public class CriminalHistoryDemographicsUpdateRequest {

	private LocalDate postUpdateDOB;
	
	private String postUpdateGivenName;
	
	private String postUpdateMiddleName;

	private String postUpdateSurName;
	
	private String preUpdateOTN;
	private String postUpdateOTN;
	
	private String preUpdateCivilSID;
	private String postUpdateCivilSID;
	
	public LocalDate getPostUpdateDOB() {
		return postUpdateDOB;
	}
	public void setPostUpdateDOB(LocalDate postUpdateDOB) {
		this.postUpdateDOB = postUpdateDOB;
	}
	public String getPostUpdateGivenName() {
		return postUpdateGivenName;
	}
	public void setPostUpdateGivenName(String postUpdateGivenName) {
		this.postUpdateGivenName = postUpdateGivenName;
	}
	public String getPostUpdateMiddleName() {
		return postUpdateMiddleName;
	}
	public void setPostUpdateMiddleName(String postUpdateMiddleName) {
		this.postUpdateMiddleName = postUpdateMiddleName;
	}
	public String getPostUpdateSurName() {
		return postUpdateSurName;
	}
	public void setPostUpdateSurName(String postUpdateSurName) {
		this.postUpdateSurName = postUpdateSurName;
	}
	public String getPreUpdateOTN() {
		return preUpdateOTN;
	}
	public void setPreUpdateOTN(String preUpdateOTN) {
		this.preUpdateOTN = preUpdateOTN;
	}
	public String getPostUpdateOTN() {
		return postUpdateOTN;
	}
	public void setPostUpdateOTN(String postUpdateOTN) {
		this.postUpdateOTN = postUpdateOTN;
	}
	public String getPreUpdateCivilSID() {
		return preUpdateCivilSID;
	}
	public void setPreUpdateCivilSID(String preUpdateCivilSID) {
		this.preUpdateCivilSID = preUpdateCivilSID;
	}
	public String getPostUpdateCivilSID() {
		return postUpdateCivilSID;
	}
	public void setPostUpdateCivilSID(String postUpdateCivilSID) {
		this.postUpdateCivilSID = postUpdateCivilSID;
	}
	

}
