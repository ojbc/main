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

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AgencyProfile implements Serializable{

	private static final long serialVersionUID = -1987473874115214441L;
	private Integer id; ;
	private String agencyOri;
	private String agencyName;
	private Boolean fbiSubscriptionQualified;
	private Boolean stateSubscriptionQualified;
	private Boolean firearmsSubscriptionQualification; 
	private Boolean cjEmploymentSubscriptionQualification; 
	
	private List<String> emails; 
	private List<String> triggeringEventCodes; 
	
	public AgencyProfile(){
		super();
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAgencyOri() {
		return agencyOri;
	}

	public void setAgencyOri(String agencyOri) {
		this.agencyOri = agencyOri;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public Boolean getFbiSubscriptionQualified() {
		return fbiSubscriptionQualified;
	}

	public void setFbiSubscriptionQualified(Boolean fbiSubscriptionQualified) {
		this.fbiSubscriptionQualified = fbiSubscriptionQualified;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public Boolean getStateSubscriptionQualified() {
		return stateSubscriptionQualified;
	}

	public void setStateSubscriptionQualified(Boolean stateSubscriptionQualified) {
		this.stateSubscriptionQualified = stateSubscriptionQualified;
	}

	public List<String> getTriggeringEventCodes() {
		return triggeringEventCodes;
	}

	public void setTriggeringEventCodes(List<String> triggeringEventCodes) {
		this.triggeringEventCodes = triggeringEventCodes;
	}

	public Boolean getFirearmsSubscriptionQualification() {
		return firearmsSubscriptionQualification;
	}

	public void setFirearmsSubscriptionQualification(
			Boolean firearmsSubscriptionQualification) {
		this.firearmsSubscriptionQualification = firearmsSubscriptionQualification;
	}

	public Boolean getCjEmploymentSubscriptionQualification() {
		return cjEmploymentSubscriptionQualification;
	}

	public void setCjEmploymentSubscriptionQualification(
			Boolean cjEmploymentSubscriptionQualification) {
		this.cjEmploymentSubscriptionQualification = cjEmploymentSubscriptionQualification;
	}

}
