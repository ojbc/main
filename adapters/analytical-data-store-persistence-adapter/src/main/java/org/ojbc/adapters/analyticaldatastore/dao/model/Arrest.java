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
package org.ojbc.adapters.analyticaldatastore.dao.model;

import java.sql.Time;
import java.util.Date;

public class Arrest {

	//pk
    private int arrestID; 
    
    //fk
    private int personID;
    
    //fk
    private int incidentID;
    
    //fk
    private int arrestingAgencyID;

    //fk
    private Integer involvedDrugID;

	private Date arrestDate;
	private Time arrestTime;
	
	//'Y if drug related, N otherwise'
	private char arrestDrugRelated;

	public int getArrestID() {
		return arrestID;
	}

	public void setArrestID(int arrestID) {
		this.arrestID = arrestID;
	}

	public int getPersonID() {
		return personID;
	}

	public void setPersonID(int personID) {
		this.personID = personID;
	}

	public int getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(int incidentID) {
		this.incidentID = incidentID;
	}

	public int getArrestingAgencyID() {
		return arrestingAgencyID;
	}

	public void setArrestingAgencyID(int arrestingAgencyID) {
		this.arrestingAgencyID = arrestingAgencyID;
	}

	public Date getArrestDate() {
		return arrestDate;
	}

	public void setArrestDate(Date arrestDate) {
		this.arrestDate = arrestDate;
	}

	public Time getArrestTime() {
		return arrestTime;
	}

	public void setArrestTime(Time arrestTime) {
		this.arrestTime = arrestTime;
	}

	public char getArrestDrugRelated() {
		return arrestDrugRelated;
	}

	public void setArrestDrugRelated(char arrestDrugRelated) {
		this.arrestDrugRelated = arrestDrugRelated;
	}

	public Integer getInvolvedDrugID() {
		return involvedDrugID;
	}

	public void setInvolvedDrugID(Integer involvedDrugID) {
		this.involvedDrugID = involvedDrugID;
	}

	
}
