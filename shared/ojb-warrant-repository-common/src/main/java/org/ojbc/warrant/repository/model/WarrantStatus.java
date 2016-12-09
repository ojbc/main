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

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WarrantStatus {

    private Integer warrantStatusID;
    private Integer warrantID;
    private String operator; 
    private String warrantStatus;
    private LocalDateTime warrantStatusTimestamp;
    
    private List<ChargeReferral> chargeReferrals;
    
	public Integer getWarrantID() {
		return warrantID;
	}
	public void setWarrantID(Integer warrantID) {
		this.warrantID = warrantID;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getWarrantStatus() {
		return warrantStatus;
	}
	public void setWarrantStatus(String warrantStatus) {
		this.warrantStatus = warrantStatus;
	}
	public LocalDateTime getWarrantStatusTimestamp() {
		return warrantStatusTimestamp;
	}
	public void setWarrantStatusTimestamp(LocalDateTime warrantStatusTimestamp) {
		this.warrantStatusTimestamp = warrantStatusTimestamp;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public List<ChargeReferral> getChargeReferrals() {
		return chargeReferrals;
	}
	public void setChargeReferrals(List<ChargeReferral> chargeReferrals) {
		this.chargeReferrals = chargeReferrals;
	}
	
	public Integer getWarrantStatusID() {
		return warrantStatusID;
	}
	public void setWarrantStatusID(Integer warrantStatusID) {
		this.warrantStatusID = warrantStatusID;
	}
}
