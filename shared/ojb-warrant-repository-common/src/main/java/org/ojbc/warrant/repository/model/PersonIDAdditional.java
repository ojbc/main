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
package org.ojbc.warrant.repository.model;

public class PersonIDAdditional {

	private Integer personIDAdditionalID;
	
	private Integer personID;

	private String personAdditionalID;

	public Integer getPersonIDAdditionalID() {
		return personIDAdditionalID;
	}

	public void setPersonIDAdditionalID(
			Integer personIDAdditionalID) {
		this.personIDAdditionalID = personIDAdditionalID;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public String getPersonAdditionalID() {
		return personAdditionalID;
	}

	public void setPersonAdditionalID(String personAdditionalID) {
		this.personAdditionalID = personAdditionalID;
	}
	
}
