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
package org.ojbc.adapters.analyticsstaging.custody.dao.model;

public class Charge {

	//pk
	private Integer chargeID;
	
	//fk
	private Integer arrestID;

	private String offenseDescriptionText;
	
	private String offenseDescriptionText1;
	
	public Integer getChargeID() {
		return chargeID;
	}

	public void setChargeID(Integer chargeID) {
		this.chargeID = chargeID;
	}

	public Integer getArrestID() {
		return arrestID;
	}

	public void setArrestID(Integer arrestID) {
		this.arrestID = arrestID;
	}

	public String getOffenseDescriptionText() {
		return offenseDescriptionText;
	}

	public void setOffenseDescriptionText(String offenseDescriptionText) {
		this.offenseDescriptionText = offenseDescriptionText;
	}

	public String getOffenseDescriptionText1() {
		return offenseDescriptionText1;
	}

	public void setOffenseDescriptionText1(String offenseDescriptionText1) {
		this.offenseDescriptionText1 = offenseDescriptionText1;
	}
}
