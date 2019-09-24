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
package org.ojbc.web.portal.arrest;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.ojbc.web.OjbcWebConstants.ArrestType;

public class ArrestDetailSearchRequest {

    private String arrestIdentification;
    private List<String> oris;
    private ArrestType arrestType; 
    private List<String> chargeIds;
    
    public ArrestDetailSearchRequest() {
    	super();
    }
    
    public ArrestDetailSearchRequest(ArrestType arrestType) {
    	this();
		this.setArrestType(arrestType);
    }
    
    public ArrestDetailSearchRequest(ArrestSearchRequest arrestSearchRequest) {
    	this();
    	this.setArrestType(arrestSearchRequest.getArrestType());
    	
    	if (arrestSearchRequest.getUserSelectedOris() != null && !arrestSearchRequest.getUserSelectedOris().isEmpty()) {
    		this.setOris(arrestSearchRequest.getUserSelectedOris());
    	}
    	else {
    		this.setOris(arrestSearchRequest.getAuthorizedOris());
    	}
    }
	public String getArrestIdentification() {
		return arrestIdentification;
	}
	public void setArrestIdentification(String arrestIdentification) {
		this.arrestIdentification = arrestIdentification;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public ArrestType getArrestType() {
		return arrestType;
	}
	public void setArrestType(ArrestType arrestType) {
		this.arrestType = arrestType;
	}
	public List<String> getOris() {
		return oris;
	}

	public void setOris(List<String> oris) {
		this.oris = oris;
	}

	public List<String> getChargeIds() {
		return chargeIds;
	}

	public void setChargeIds(List<String> chargeIds) {
		this.chargeIds = chargeIds;
	}
}